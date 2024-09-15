package com.czx.demoj.netty.timeServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TimeClientHandler.class);
    private static int fragmentCount = 0;
    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.debug("handlerAdded");
        buf = ctx.alloc().buffer(4);
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
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.debug("handlerRemoved");
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("channelRead");
        log.debug("{} {} {}", buf.readerIndex(), buf.writerIndex(), buf.capacity());
        ByteBuf msgBuf = (ByteBuf) msg;
        buf.writeBytes(msgBuf);
        msgBuf.release();

        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            buf.clear();
            // ctx.close(); // 关闭 handler context
        } else {
            fragmentCount++;
            log.debug("检测到分片问题！ {}", fragmentCount);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught", cause);
        cause.printStackTrace();
        ctx.close();
    }


}
