package com.czx.demo.concurrent.ifThreadTolerateException;

public class main {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException();
        });

        thread.start();
        Thread.sleep(1000);

        System.out.println("time is " + System.currentTimeMillis() + ", another thread alive: " + thread.isAlive());

        Thread.sleep(1000);
        Thread.onSpinWait();
        System.out.println("time is " + System.currentTimeMillis() + ", another thread alive: " + thread.isAlive());
        thread.join();
        System.out.println("end, another thread: " + thread.getState() + ", now time is " + System.currentTimeMillis());
    }
}
