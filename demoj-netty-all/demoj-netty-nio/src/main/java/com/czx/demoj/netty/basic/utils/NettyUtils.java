package com.czx.demoj.netty.basic.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class NettyUtils {
    public static void startSimpleServerSocketChannel(int port, ChannelHandler handler) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(handler)
                .bind(8080);
    }

    public static void log(ByteBuf buf) {
        int length = buf.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder sb = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buf.readerIndex())
                .append(" write index:").append(buf.writerIndex())
                .append(" capacity:").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(sb, buf);
        System.out.println(sb.toString());
    }
}
