package com.czx.demoj.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

import static com.czx.demoj.zk.curator.CuratorCrudTest.createClient;



public class CuratorDistributedLockTest {


    static HashMap<Integer, Integer> map = new HashMap<>();


    private final PathChildrenCache pathChildrenCache;


    public CuratorDistributedLockTest() throws Exception {
        setCreateClient();
        pathChildrenCache = new PathChildrenCache(
                client, "/ticket_lock", false
        );
        pathChildrenCache.start();
    }

    private static final Logger log = LoggerFactory.getLogger(CuratorDistributedLockTest.class);

    static class Ticket {
        private static final Logger log = LoggerFactory.getLogger(Ticket.class);
        private int ticket = 1000;

        int sell() {
            int soldTicket = ticket--;
            log.debug("序号 {} 的票被卖出去了", soldTicket);
            return soldTicket;
        }

        boolean noTicket() {
            return ticket <= 0;
        }
    }

    class Buyer implements Runnable {

        Ticket ticket;

        public Buyer(Ticket ticket) throws Exception {
            this.ticket = ticket;
        }

        /**
         * 抢票场景
         */
        @Override
        public void run() {


            while (true) {
                if (ticket.noTicket()) {
                    break;
                }

                long st = System.currentTimeMillis();

                Runnable unlock = null;
                try {
                    unlock = acquireDistributedLock();

                    if (ticket.noTicket()) {
                        break;
                    }
                    int sell = ticket.sell();
                    log.debug("sell: {}", sell);

                    if (map.containsKey(sell)) {
                        map.put(sell, map.get(sell) + 1);
                        throw new RuntimeException("double");
                    } else {
                        map.put(sell, 1);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    unlock.run();

                }
            }
            log.debug("票被卖完了！");
        }
    }


    @Test
    public void testZkLock() throws Exception {

        // clear
        try {
            client.delete().deletingChildrenIfNeeded().forPath("/ticket_lock");
        } catch (Exception e) {
        }

        // ticket sys
        Ticket ticket = new Ticket();

        long now = System.currentTimeMillis();

        // consumer
        CuratorDistributedLockTest feizhuTest = new CuratorDistributedLockTest();
        CuratorDistributedLockTest quNaErTest = new CuratorDistributedLockTest();
        CuratorDistributedLockTest meituanTest = new CuratorDistributedLockTest();

        feizhuTest.setCreateClient();
        quNaErTest.setCreateClient();
        meituanTest.setCreateClient();

        Buyer feizhu = feizhuTest.new Buyer(ticket);
        Buyer quNaEr = quNaErTest.new Buyer(ticket);
        Buyer meituan = meituanTest.new Buyer(ticket);

        Thread t1 = new Thread(feizhu, "飞猪");
        Thread t2 = new Thread(quNaEr, "去哪儿");
        Thread t3 = new Thread(meituan, "美团");

        t1.start();
        t2.start();
        t3.start();
        t1.join();
        t2.join();
        t3.join();
        log.debug("{}", map.size());


        feizhuTest.close();
        quNaErTest.close();
        meituanTest.close();
    }

    CuratorFramework client;

    @Before
    public void setCreateClient() throws Exception {
        client = createClient();
    }

    @After
    public void close() {
        if (client != null)
            client.close();
    }

    final Object lock = new Object();

    volatile boolean locked = false;

    public <T> Runnable acquireDistributedLock() throws Exception {
        String s;

//        Timer.register("try lock");
        while (true) {
            s = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath("/ticket_lock/lock");

            List<String> list = client.getChildren().forPath("/ticket_lock");

            list.sort(String::compareTo);

//            Timer.register("split");
            String[] split = s.split("/");
            String lck = split[split.length - 1];

            if (list.isEmpty() || !list.contains(lck)) continue;

//            Timer.acc("split");

            if (!list.getFirst().equals(lck)) {
                log.debug("{} 不为最小", lck);
                int index = list.indexOf(lck);

                String lockSmaller = list.get(index - 1);
                String path = "/ticket_lock/" + lockSmaller;
                log.debug("{} waiting {} release", lck, path);


                pathChildrenCache.getListenable().addListener((client, event) -> {
                    if (event.getData() != null && event.getData().getPath().equals(path)) {
                        if (event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                            synchronized (this) {
                                this.notifyAll();
                            }
                        }
                    }
                });
//                Timer.register("reg node cache");
//                NodeCache nodeCache = new NodeCache(client, path, false);
////                Timer.acc("reg node cache");
//                NodeCacheListener nodeCacheListener = new NodeCacheListener() {
//                    @Override
//                    public void nodeChanged() throws Exception {
////                        Timer.register("event listener");
//                        if (nodeCache.getCurrentData() == null) {
//                            synchronized (lock) {
//                                List<String> list = nodeCache.getClient().getChildren().forPath("/ticket_lock");
//                                list.sort(String::compareTo);
//                                log.debug("event of {} listening to {} triggered, list: {}", lck, path, list);
//                                if (list.getFirst().equals(lck)) {
//                                    log.debug("列表第一个为 lck 匹配，进入锁");
//                                    locked = true;
//                                    lock.notifyAll();
//                                    nodeCache.close();
//                                } else {
//                                    log.warn(
//                                            "锁获取异常，当前锁列表为：{}，监听中的锁序号为 {}，重新添加监听器并启动", list, lck
//                                    );
//                                    nodeCache.getListenable().removeListener(this);
//                                    nodeCache.getListenable().addListener(this);
//                                }
//
//                            }
//                        }
////                        Timer.acc("event listener");
//                    }
//                };


//                nodeCache.getListenable().addListener(nodeCacheListener);
//                nodeCache.start();


                log.debug("{} 锁进入等待通知状态", lck);
//                Timer.register("wait");
                synchronized (this) {
                    while (client.checkExists().forPath(path) != null) {
                        this.wait();
                    }
                }
//                Timer.acc("wait");
                log.debug("{} 锁获取成功了！", lck);
            } else {
                log.debug("{} 确实是最小，跳过监听，直接获取成功！", lck);
            }
            break;
        }
        String finalS1 = s;
//        Timer.acc("try lock");
        return () -> {
            try {
                log.debug("归还锁，删除 path：{}", finalS1);

                client.delete().deletingChildrenIfNeeded().forPath(finalS1);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

}
