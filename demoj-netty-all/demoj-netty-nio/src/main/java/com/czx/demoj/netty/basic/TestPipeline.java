package com.czx.demoj.netty.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class TestPipeline {
    private static final Logger log = LoggerFactory.getLogger(TestPipeline.class);

    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("h1, {}", System.identityHashCode(ctx));
                                log.debug("h1, inbound channel {}", System.identityHashCode(ctx.channel()));
                                super.channelRead(ctx, msg);
                            }
                        }).addLast("h2", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("h2, {}", System.identityHashCode(ctx));
                                log.debug("h2, inbound channel {}", System.identityHashCode(ctx.channel()));
                                super.channelRead(ctx, msg);
                            }
                        }).addLast("h3", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.debug("h3, {}", System.identityHashCode(ctx));
                                log.debug("h3, inbound channel {}", System.identityHashCode(ctx.channel()));
                                ctx.channel().writeAndFlush(ctx.alloc().buffer().writeBytes("123".getBytes()));
                                super.channelRead(ctx, msg);
                            }
                        }).addLast("h4", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("h4, Outbound ctx {}", System.identityHashCode(ctx));
                                log.debug("h4, Outbound channel {}", System.identityHashCode(ctx.channel()));
                                super.write(ctx, msg, promise);
                            }
                        }).addLast("h5", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("h5, {}", System.identityHashCode(ctx));
                                ByteBuf msgBuf = (ByteBuf) msg;
                                ByteBuf buf = ctx.alloc().buffer(msgBuf.capacity() * 2);
                                buf.writeBytes(msgBuf);
                                buf.writeBytes(msgBuf.readerIndex(0));

                                log.debug("h5, buf.toString {}",buf.toString(Charset.defaultCharset()));
                                log.debug("h5, buf.readerIndex {}",buf.readerIndex());

                                super.write(ctx, buf, promise);
                            }
                        }).addLast("h6", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                log.debug("h6, {}", System.identityHashCode(ctx));
                                ByteBuf msgBuf = (ByteBuf) msg;
                                ByteBuf buf = ctx.alloc().buffer(msgBuf.capacity() * 2);
                                buf.writeBytes(msgBuf);
                                buf.writeBytes(msgBuf.readerIndex(0));
                                super.write(ctx, buf, promise);
                            }
                        });
                    }

                });

        Channel channel = serverBootstrap.bind(8080).sync().channel();

        log.debug("server socket channel : {}", System.identityHashCode(channel));
        channel.closeFuture().sync();
        log.debug("closed");
    }
}
