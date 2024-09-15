package com.czx.demoj.netty.eventLoopBinding;

import ch.qos.logback.classic.spi.EventArgUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class EventLoopClient {
    private static final Logger log = LoggerFactory.getLogger(EventLoopClient.class);


    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        group.forEach((loop) -> log.debug("{}", ((SingleThreadEventExecutor) loop).threadProperties().name()));
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("127.0.0.1", 8080);

        Channel channel = channelFuture.sync().channel();
        // channelFuture.addListener((ChannelFutureListener) future -> {
        log.debug("{}", channel);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if ("q".equals(line)) {
                    // try {
                    ChannelFuture closeFuture = channel.close();
                    closeFuture.addListener((ChannelFutureListener) (future) -> {
                        log.debug("处理关闭之后的操作");
                        group.shutdownGracefully();
                    });
                    // } catch (InterruptedException e) {
                    //     throw new RuntimeException(e);
                    // }

                    break;
                }
                channel.writeAndFlush(line);
            }
        }).start();
        // });

        System.out.println();
    }
}
