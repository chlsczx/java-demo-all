package com.czx.demoj.netty.basic.testFuturePromise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class TestPromise {
    private static final Logger log = LoggerFactory.getLogger(TestPromise.class);

public static void main(String[] args) throws ExecutionException, InterruptedException {
    EventLoop eventLoop = new NioEventLoopGroup().next();
    DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);

    new Thread(() -> {
        System.out.println("start calc");
        try {
            Thread.sleep(1000);
            int i = 1 / 0;
            promise.setSuccess(80);
        } catch (Exception e) {
            // e.printStackTrace();
            promise.setFailure(e);
        }

    }).start();

    log.debug("wait for result");
    try {
        log.debug("result is {}", promise.get());
    } catch (Exception e) {
        log.error(e.getMessage());
    }
}
}
