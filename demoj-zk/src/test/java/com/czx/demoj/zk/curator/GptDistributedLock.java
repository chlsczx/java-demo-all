package com.czx.demoj.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GptDistributedLock {
    private static final Logger log = LoggerFactory.getLogger(GptDistributedLock.class);
    private final CuratorFramework client;
    private final String lockPath;
    private String currentLockNode;
    public static int ticket = 1000;
    private final PathChildrenCache pathChildrenCache;

    public GptDistributedLock(CuratorFramework client, String lockPath) throws Exception {
        this.client = client;
        this.lockPath = lockPath;
        this.pathChildrenCache = new PathChildrenCache(client, lockPath, false);
        this.pathChildrenCache.start();
    }

    public void lock() throws Exception {
        // Step 1: Create an ephemeral sequential node
        currentLockNode = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(lockPath + "/lock_");

        // Step 2: Try to acquire the lock
        acquireLock();
    }

    private void acquireLock() throws Exception {
        while (true) {
            // Step 3: Get all children of the lock path and sort them
            List<String> lockNodes = client.getChildren().forPath(lockPath);
            Collections.sort(lockNodes);

            // Step 4: Check if current node is the smallest node
            int index = lockNodes.indexOf(currentLockNode.substring(lockPath.length() + 1));
            log.debug("lockNodes: {}", lockNodes);
            log.debug("currentLockNode {} 尝试获取锁", currentLockNode);
            if (index == 0) {
                // We have the lock
                log.debug("Lock acquired by {} ", currentLockNode);
                return;
            }

            // Step 5: Watch the node just before the current node
            String previousNode = lockNodes.get(index - 1);
            final String previousNodePath = lockPath + "/" + previousNode;
            log.debug("currentLockNode {} 尝试失败，监听：{}", currentLockNode, previousNodePath);


            pathChildrenCache.getListenable().addListener((client, event) -> {
                if (event.getData() != null && event.getData().getPath().equals(previousNodePath)) {
                    if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                        synchronized (this) {
                            this.notifyAll();
                        }
                    }
                }
            });
            synchronized (this) {
                // Check if the previous node still exists before waiting
                if (client.checkExists().forPath(previousNodePath) != null) {
                    log.debug("{} 前序节点依然存在，继续 wait", previousNodePath);
                    this.wait();
                }
            }
        }
    }

    public void unlock() throws Exception {
        // Step 6: Delete the current lock node
        if (currentLockNode != null) {
            client.delete().forPath(currentLockNode);
            log.debug("Lock released by " + currentLockNode);
            currentLockNode = null;
        }
    }

    public static void main(String[] args) throws Exception {
        ((Runnable) (() -> {
            CuratorFramework client = CuratorFrameworkFactory.builder()
                    .connectString("localhost:2181")
                    .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                    .namespace("gpt")
                    .build();
            client.start();
            try {
                client.delete().guaranteed().deletingChildrenIfNeeded().forPath("/gpt-distributed-lock");
                log.debug("启动清理成功");
            } catch (Exception e) {
            }
        })).run();

        Set<Integer> set = new HashSet<>();

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                CuratorFramework client = CuratorFrameworkFactory.builder()
                        .connectString("localhost:2181")
                        .retryPolicy(new ExponentialBackoffRetry(3000, 3))
                        .sessionTimeoutMs(60 * 1_000)
                        .connectionTimeoutMs(15 * 1_000)
                        .namespace("gpt")
                        .build();
                client.start();

                GptDistributedLock lock;
                try {
                    lock = new GptDistributedLock(client, "/gpt-distributed-lock");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                while (ticket > 0) {
                    try {
                        lock.lock();
                        if (ticket > 0) {
                            log.debug("卖出序号 {}", ticket);
                            if (set.contains(ticket)) {
                                throw new RuntimeException("double sell one ticket");
                            }
                            set.add(ticket);
                            ticket--;
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        log.info("unlock in finally");
                        try {
                            lock.unlock();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                client.close();
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        log.debug("set size: {}", set.size());
    }
}
