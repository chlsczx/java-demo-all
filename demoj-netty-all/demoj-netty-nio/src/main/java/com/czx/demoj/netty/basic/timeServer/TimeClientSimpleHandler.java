package com.czx.demoj.netty.basic.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class TimeClientSimpleHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TimeClientSimpleHandler.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.debug("handlerAdded");
        new Thread(() -> {
            while (true) {
                ByteBuf buffer = ctx.alloc().buffer(4);
                // log.debug("{} {} {}", buffer.readerIndex(), buffer.writerIndex(), buffer.capacity());
                buffer.writeInt(0);
                // log.debug("write ctx in interval");
                // log.debug("{} {} {}", buffer.readerIndex(), buffer.writerIndex(), buffer.capacity());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ctx.writeAndFlush(buffer);
            }
        }).start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("channelRead");
        ByteBuf buf = (ByteBuf) msg;
        log.debug("{} {} {}", buf.readerIndex(), buf.writerIndex(), buf.capacity());

        if(buf.readableBytes() < 4) throw new RuntimeException("readableBytes < 4");

        long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
        System.out.println(new Date(currentTimeMillis));
        buf.release();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
