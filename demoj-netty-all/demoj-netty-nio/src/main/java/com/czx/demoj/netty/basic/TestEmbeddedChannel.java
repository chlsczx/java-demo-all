package com.czx.demoj.netty.basic;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestEmbeddedChannel {
    private static final Logger log = LoggerFactory.getLogger(TestEmbeddedChannel.class);

    public static void main(String[] args) {
        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("1");
                super.channelRead(ctx, msg);
            }
        };
        ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("2");
                super.channelRead(ctx, msg);
            }
        };
        ChannelOutboundHandlerAdapter h3 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void read(ChannelHandlerContext ctx) throws Exception {
                log.debug("3 read");
                super.read(ctx);
            }

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("3 write");
                super.write(ctx, msg, promise);
            }
        };
        ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void read(ChannelHandlerContext ctx) throws Exception {
                log.debug("4 read");
                super.read(ctx);
            }
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                log.debug("4 write");
                super.write(ctx, msg, promise);
            }
        };
        ChannelInboundHandlerAdapter h5 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                log.debug("5");
                super.channelRead(ctx, msg);
            }
        };
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(h1, h2, h3, h4,h5);
        log.debug("in");
        embeddedChannel.writeInbound(
                ByteBufAllocator.DEFAULT.buffer().writeBytes("hello".getBytes())
        );
        log.debug("out");
        embeddedChannel.writeOutbound(
                ByteBufAllocator.DEFAULT.buffer().writeBytes("world".getBytes())
        );
    }
}
