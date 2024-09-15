package com.czx.demoj.netty.basic.timeServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class TimeClient {
    private static final Logger log = LoggerFactory.getLogger(TimeClient.class);

    public static void main(String[] args) throws InterruptedException, IOException {
        String host = Optional.ofNullable(args.length > 0 ? args[0] : null).orElse("127.0.0.1");
        int port = Integer.parseInt(Optional.ofNullable(args.length > 1 ? args[1] : null).orElse("8080"));

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientSimpleHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();
            Channel channel = f.channel();
            Scanner scanner = new Scanner(System.in);
            while(scanner.nextLine() != null) {
                log.debug("write");
                channel.writeAndFlush("1");
            }

            // log.debug("close future before");
            // channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
