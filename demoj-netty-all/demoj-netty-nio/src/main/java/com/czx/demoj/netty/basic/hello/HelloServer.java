package com.czx.demoj.netty.basic.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class HelloServer {
    private static final Logger log = LoggerFactory.getLogger(HelloServer.class);

    public static void main(String[] args) {
        ChannelHandler handler = new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) {
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf buf = (ByteBuf) msg;
                        log.debug(buf.toString(Charset.defaultCharset()));
                    }
                });
            }
        };


        new ServerBootstrap()
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(handler)
                .bind(8080);
    }

    private static void startServer() {
        // 1. 服务器启动器，负责组装 netty 组件，启动服务器
        new ServerBootstrap()
                // event loop group: 代表 event loop，container for thread and selector，
                .group(new NioEventLoopGroup())
                // 使用什么类型的 channel
                // 种类有 OIO BIO / NIO and optimized event channel for linux(epoll) and mac(k queue) etc.
                .channel(NioServerSocketChannel.class)
                /*
                 specific work: boss 负责处理连接，worker(child) 负责处理读写，决定了 worker(child) 能执行哪些操作(handler)
                 */
                .childHandler(
                        /* channel 代表和客户端进行数据读写的通道；
                           Initializer 初始化，负责添加别的 handler（本身也是handler）*/
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                // 添加 handler，流水线操作，先 decode，再 inbound（入站）handler
                                nioSocketChannel.pipeline().addLast(new StringDecoder());
                                nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });

                                /**
                                 * 总体 handler 的处理职责结构：
                                 *
                                 * childHandler
                                 * - ChannelInitializer
                                 *   处理 nioSocketChannel，如何处理：
                                 *   - StringDecoder
                                 *   - ChannelInboundHandlerAdapter
                                 *     处理 inbound msg
                                 */
                            }
                        })
                .bind(8080);
    }
}
