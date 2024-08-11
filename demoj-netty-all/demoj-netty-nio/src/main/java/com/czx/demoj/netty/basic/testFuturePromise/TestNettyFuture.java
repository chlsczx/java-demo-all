package com.czx.demoj.netty.basic.testFuturePromise;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class TestNettyFuture {
    private static final Logger log = LoggerFactory.getLogger(TestNettyFuture.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        Future<Integer> future = eventLoop.submit(() -> {
            log.debug("执行计算");
            Thread.sleep(1000);
            // throw new IllegalArgumentException("wrong argument");
            return 80;
        });

        // 同步方式获取结果
        // log.debug("等待结果");
        // Integer i = future.get();
        // log.debug("结果是 {}", i);

        // 异步方式
        future.addListener((GenericFutureListener<? extends Future<? super Integer>>) (f) -> {
                    if (f.isSuccess()) {
                        log.debug("result is {}", f.getNow());
                    } else {
                        log.debug("failure");
                    }
                }
        );

    }
}
