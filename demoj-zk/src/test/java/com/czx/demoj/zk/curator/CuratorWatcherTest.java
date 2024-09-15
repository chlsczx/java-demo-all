package com.czx.demoj.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static com.czx.demoj.zk.curator.CuratorCrudTest.createClient;

public class CuratorWatcherTest {

    private static final Logger log = LoggerFactory.getLogger(CuratorWatcherTest.class);
    CuratorFramework client;

    @Before
    public void setCreateClient() {
        client = createClient();
    }

    @After
    public void close() {
        if (client != null)
            client.close();
    }

    /**
     * Test {@link org.apache.curator.framework.recipes.cache.NodeCache NodeCache}
     */
    @Test
    public void testNodeCache() throws Exception {
        String s = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/app1/lc");
        log.debug("创建了 {} 并监听", s);
        // create NodeCache instance
        NodeCache nodeCache = new NodeCache(
                client,
                s,
                false
        );

        // register watcher
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.debug("节点变化");

                byte[] data = nodeCache.getCurrentData().getData();
                log.debug("New data: {}", new String(data, StandardCharsets.UTF_8));
            }
        });

        // start watch
        // buildInitial true: get initial view of the node
        nodeCache.start(true);

        (new Scanner(System.in)).nextLine();
    }

}
