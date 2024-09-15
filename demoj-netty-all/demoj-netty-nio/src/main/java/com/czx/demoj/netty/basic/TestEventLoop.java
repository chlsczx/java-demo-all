package com.czx.demoj.netty.basic;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestEventLoop {
    private static final Logger log = LoggerFactory.getLogger(TestEventLoop.class);

    public static void main(String[] args) throws IOException {
        // create event loop group
        EventLoopGroup group = new NioEventLoopGroup(2);

        // next event loop：一个 channel 由一个 event loop 负责到底
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        group.next().submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("ok");
        });

        group.next().scheduleAtFixedRate(() -> {
            log.debug("task");
        }, 0, 1, TimeUnit.SECONDS);

        log.debug("main");

        System.in.read();
        group.shutdownGracefully();
    }
}
