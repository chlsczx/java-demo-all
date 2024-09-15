package com.czx.demoj.netty.basic;

import com.czx.demoj.netty.basic.utils.NettyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class TestByteBuf {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        NettyUtils.log(buffer);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 300; i ++) {
            stringBuilder.append("a");
        }
        buffer.writeBytes(stringBuilder.toString().getBytes());
        NettyUtils.log(buffer);
    }
}
