package com.czx.demoj.zk.curator;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class NoLockTicket {

    private static final Logger log = LoggerFactory.getLogger(NoLockTicket.class);
    private static int ticket = 100000;

    volatile static Map<String, List<Integer>> map = new ConcurrentHashMap<>();

    public static void main(String[] args) throws InterruptedException, ExecutionException {


        Callable<List<Integer>> buyer = () -> {
            ArrayList<Integer> list = new ArrayList<>();
            while (true) {
                if (ticket > 0) {
                    int bought = ticket--;
                    log.debug("购买了 {}", bought);
                    list.add(bought);
                } else break;
            }
            map.put(Thread.currentThread().getName(), list);
            log.debug("{} 放置结束", Thread.currentThread().getName());
            return list;
        };

        Runnable buy = () -> {
            try {
                buyer.call();
                log.debug("buyer {} 结束", Thread.currentThread().getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Thread t1 = new Thread(buy, "1");
        Thread t2 = new Thread(buy, "2");
        Thread t3 = new Thread(buy, "3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

//        List<Future<List<Integer>>> futures = executorService.invokeAll(List.of(buyer, buyer, buyer));
        int all = 0;
//        for (Future<List<Integer>> future : futures) {
//            List<Integer> integers = future.get();
//            all += integers.size();
//            System.out.println(integers.size());
//        }
        log.debug("map.size: {}", map.size());
        log.debug("{}", map.keySet());
        log.debug("{}", map.get("1").size());
        log.debug("{}", map.get("2").size());
        log.debug("{}", map.get("3").size());
        all += map.get("1").size();
        all += map.get("2").size();
        all += map.get("3").size();
        log.debug("{}", all);
    }
}
