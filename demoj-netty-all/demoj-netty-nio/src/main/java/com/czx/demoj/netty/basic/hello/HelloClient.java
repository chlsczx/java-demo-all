package com.czx.demoj.netty.hello;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        Channel ch = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                }).connect(new InetSocketAddress("localhost", 8080))
                .sync()
                .channel();


        // Scanner scanner = new Scanner(System.in);
        // while (scanner.hasNextLine()) {
        //     String msg = scanner.nextLine();
        //     ch.writeAndFlush(msg);
        // }
        System.out.println("");
    }
}
