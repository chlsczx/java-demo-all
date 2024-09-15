package com.czx.demoj.nio.messageEdgeSolve;

import com.czx.demoj.nio.utils.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class MessageEdgeUtils {

    private static final Logger log = LoggerFactory.getLogger(MessageEdgeUtils.class);

    /**
     * 如何处理消息边界，=》 获取消息头（使用固定长度 buffer）生成长度“合适”的 buffer 去接收
     */
    public static ByteBuffer handleSingleSocketMessage(final SocketChannel socketChannel) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        final int lengthBit = 32, headerBit = 32, headerBytes = headerBit / 8;
        int startPowerLevel = lengthBit / 8;

        ByteBuffer lengthBuffer = ByteBuffer.allocate(headerBytes);
        for (int acc = 0; acc < headerBytes; ) {
            int read = socketChannel.read(lengthBuffer);
            log.debug("read: {}", read);
            BufferUtils.debugAll(lengthBuffer);
            if (read == -1) {
                return null;
            }
            Thread.sleep(1000);
            acc += read;
            log.debug("读入 {} bytes 长度信息，总读入：{}", read, acc);
            if (acc > headerBytes) {
                throw new RuntimeException("acc exceeds limit 4, wrong thought.");
            }
        }
        lengthBuffer.flip();
        // using byte to determine the length

        long resultLength = 0;
        while (lengthBuffer.hasRemaining()) {
            if (startPowerLevel == 0) {
                throw new RuntimeException("Read bytes exceed 4! wtf!");
            }
            byte b = lengthBuffer.get();
            log.debug("b : {}", b);
            resultLength += (long) (b * Math.pow(2, 8 * (--startPowerLevel)));
        }

        if (resultLength > Integer.MAX_VALUE) {
            throw new RuntimeException("Read bytes exceeds Integer.MAX_VALUE!");
        }
        int intResultLength = (int) resultLength;
        log.debug("resultLength: {}, to int: {}", resultLength, intResultLength);
        ByteBuffer messageBuffer = ByteBuffer.allocate(intResultLength);

        for (int acc = 0; acc < intResultLength; ) {
            acc += socketChannel.read(messageBuffer);
            if (acc > intResultLength) {
                throw new RuntimeException("Read message bytes exceeds intResultLength!");
            }
        }

        CharBuffer messageCharBuffer = StandardCharsets.UTF_8.decode(messageBuffer).flip();
        messageBuffer.rewind();

        System.out.println(messageCharBuffer.getClass());
        String message = messageCharBuffer.toString();
        log.debug("message: {}", message);
        return messageBuffer;
    }
}
