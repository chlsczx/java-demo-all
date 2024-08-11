package com.czx.demoj.netty.basic.testFuturePromise;

import java.util.concurrent.*;

public class TestJdkFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(1000);
                return 50;
            }
        });

        Integer i = future.get();
        System.out.println(i);
        executorService.close();
    }
}
