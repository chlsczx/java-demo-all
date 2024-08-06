package com.czx.demo.concurrent.producerAndConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    // Blocking Queue 在以下情况下会阻塞：1. 当队列满了使用 put 2. 当队列空了使用 take
    private BlockingQueue<Integer> queue;
    private static AtomicInteger productionId = new AtomicInteger(0);

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            while (true) {
                queue.put(productionId.incrementAndGet());
                System.out.println("Put one product, queue now:" + queue.size());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10, true);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        Thread producerThread = new Thread(producer);
        Thread consumerThread1 = new Thread(consumer);
        Thread consumerThread2 = new Thread(consumer);
        Thread consumerThread3 = new Thread(consumer);
        producerThread.start();
        consumerThread1.start();
        consumerThread2.start();
        consumerThread3.start();
        producerThread.join();
    }
}
