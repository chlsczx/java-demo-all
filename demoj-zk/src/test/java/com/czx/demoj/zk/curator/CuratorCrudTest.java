package com.czx.demoj.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class CuratorCrudTest {

    private static final Logger log = LoggerFactory.getLogger(CuratorCrudTest.class);
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

    public static CuratorFramework createClient() {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .namespace("czx_test_zk")
                .retryPolicy(new ExponentialBackoffRetry(3_000, 10))
                .sessionTimeoutMs(60 * 1_000)
                .connectionTimeoutMs(15 * 1_000)
                .build();

        client.start();
        return client;
    }

    @Test
    public void testDelete() throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath("/app1/lc0000000001");
    }

    @Test
    public void testCreate() throws Exception {
        String s = client.create()
                .creatingParentsIfNeeded()
                .forPath("/app1/lc0000000001");
    }

    @Test
    public void testCreateEphemeral() throws Exception {
        String s = client.create().withMode(CreateMode.EPHEMERAL).forPath("/app1");
    }

    @Test
    public void testCreateWithParentNode() throws Exception {
        String s = client.create().creatingParentsIfNeeded()
                .forPath("/app1");
    }

    @Test
    public void testCreateWithData() throws Exception {
        client.create().forPath("/app1", "czx_test".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetData() throws Exception {
        byte[] bytes = client.getData().forPath("/app1");
        log.debug(new String(bytes, StandardCharsets.UTF_8));
    }

    @Test
    public void testGetChildren() throws Exception {
        List<String> list = client.getChildren().forPath("/");
        log.debug("{}", list);
    }

    @Test
    public void testState() throws Exception {
        Stat stat = new Stat();
        log.debug("{}", stat);
        byte[] bytes = client.getData().storingStatIn(stat).forPath("/app1");
        log.debug("data: {}", new String(bytes));
        log.debug("{}", stat);
    }

    @Test
    public void testSetDate() throws Exception {
        client.setData().forPath("/app1", "czx_test111".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testSetDateCasVersion() throws Exception {
        int version;
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/app1");
        log.debug("Version: {}", stat.getVersion());
        version = stat.getVersion();
        client.setData().withVersion(version).forPath("/app1", "czx_test111".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testDeleteGuaranteed() throws Exception {
        client.delete().guaranteed().forPath("/app1");
    }

    @Test
    public void testDeleteCallback() throws Exception {
        client.delete().guaranteed()
                .deletingChildrenIfNeeded()
                .inBackground(
                        (client, event) -> {
                            log.debug("delete with callback");
                            log.debug("event: {}", event);
                        }
                ).forPath("/app1");

        Thread.sleep(10);
    }

}
