package com.czx.demoj.zk.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.czx.demoj.zk.curator.CuratorCrudTest.createClient;

public class CuratorInterProcessMutexTest implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(CuratorInterProcessMutexTest.class);
    private static int ticket = 1000;
    private static HashMap<Integer, Integer> map = new HashMap<>();

    private InterProcessMutex lock;

    private CuratorFramework client;

    public void closeClient() {
        client.close();
    }

    public CuratorInterProcessMutexTest() {
        client = createClient();
        lock = new InterProcessMutex(client, "/czx_test_zk/inter_mutex");
    }

    @Test
    public void testMutex() throws InterruptedException {
        CuratorInterProcessMutexTest feizhu = new CuratorInterProcessMutexTest();
        CuratorInterProcessMutexTest xiecheng = new CuratorInterProcessMutexTest();
        CuratorInterProcessMutexTest quNaEr = new CuratorInterProcessMutexTest();
        Thread thread1 = new Thread(feizhu, "飞猪");
        Thread thread2 = new Thread(xiecheng, "携程");
        Thread thread3 = new Thread(quNaEr, "去哪儿");

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        feizhu.closeClient();
        xiecheng.closeClient();
        quNaEr.closeClient();

        int size = map.size();
        System.out.println(size);

    }


    @Override
    public void run() {
        while (true) {
            try {
                lock.acquire();
                if (ticket > 0) {
                    log.debug("{}", ticket);
                    if (map.containsKey(ticket)) {
                        map.put(ticket, map.get(ticket) + 1);
                        throw new RuntimeException("aaaa");
                    } else map.put(ticket, 1);
                    ticket--;
                } else break;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
