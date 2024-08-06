package com.czx.demo.concurrent.producerAndConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {

    private final BlockingQueue<Integer> queue;
    private static int expect = 0;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public void consume(int value) {
        System.out.println("expect matched, consumed: " + value);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer currentExpect;
                Integer value;
                synchronized (queue) {
                    value = queue.take();
                    currentExpect = ++expect;
                }
                if (!currentExpect.equals(value)) {
                    System.out.println("!!!!!!!!!!!!!!!!! expect mismatch, got id: " + value + ", expect id: " + currentExpect);
                } else {
                    this.consume(value);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
