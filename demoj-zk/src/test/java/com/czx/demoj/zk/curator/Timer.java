package com.czx.demoj.zk.curator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Timer {

    private static final Logger log = LoggerFactory.getLogger(Timer.class);

    static Map<String, List<Long>> map = new ConcurrentHashMap<>();

    static void initSeq(String seq) {
        map.computeIfAbsent(seq, k -> {
            List<Long> list = new ArrayList<>(2);
            list.add(0L);
            list.add(0L);
            return list;
        });
    }

    static void register(String seq) {
        register(seq, Thread.currentThread().threadId());
    }

    static void register(String seq, long threadId) {
        initSeq(seq);

        String threadSeqId = seq + "_" + threadId;
        if (map.containsKey(threadSeqId)) {
            throw new RuntimeException("Last seq not over detected in new sql register");
        }
        initSeq(threadSeqId);
        List<Long> list = map.get(threadSeqId);

        log.debug(list.toString());
        // register time
        list.set(0, (Long) System.nanoTime());
    }

    static void acc(String seq) {
        acc(seq, Thread.currentThread().threadId());
    }

    static void acc(String seq, long threadId) {
        String threadSeqId = seq + "_" + threadId;

        long cost = System.nanoTime() - map.get(threadSeqId).getFirst();
        map.remove(threadSeqId);
        List<Long> list = map.get(seq);

        synchronized (list) {
            list.set(0, list.get(0) + cost);
            list.set(1, list.get(1) + 1);
        }
    }

    static String log() {
        StringBuilder log = new StringBuilder("\n");
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach((e) -> {
            log.append(e.getKey() + ": " + e.getValue() + "\n");
        });
        return log.toString();
    }

    public static void main(String[] args) throws InterruptedException {
        Timer.register("hello");

        Thread t1 = new Thread(() -> {
            Timer.register("hello");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        t1.start();
        t1.join();

        Timer.acc("hello", t1.threadId());

        Thread.sleep(2000);

        Timer.acc("hello");

        log.debug(Timer.log());
    }
}
