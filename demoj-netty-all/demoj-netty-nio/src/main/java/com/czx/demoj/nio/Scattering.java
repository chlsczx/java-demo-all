package com.czx.demoj.nio;

import com.czx.demoj.nio.utils.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Scattering {
    public static void main(String[] args) {
        scatteringWrite();
    }

    private static void scatteringRead() {
        File resourceFile = BufferUtils.getResourceFile("data.txt");
        Objects.requireNonNull(resourceFile);
        try (RandomAccessFile file = new RandomAccessFile(resourceFile, "rw")) {
            // try (FileChannel channel = new FileInputStream(resourceFile).getChannel()) {
            FileChannel channel = file.getChannel();
            ByteBuffer buf1 = ByteBuffer.allocate(5);
            ByteBuffer buf2 = ByteBuffer.allocate(1);
            ByteBuffer buf3 = ByteBuffer.allocate(3);
            channel.read(new ByteBuffer[]{buf1, buf2, buf3});
            BufferUtils.debugAll(buf1);
            BufferUtils.debugAll(buf2);
            BufferUtils.debugAll(buf3);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void scatteringWrite() {
        try (RandomAccessFile file = new RandomAccessFile("scattering_write.txt", "rw")) {
            long start = file.length();
            ByteBuffer helloBuf = StandardCharsets.UTF_8.encode("你好");
            ByteBuffer worldBuf = StandardCharsets.UTF_8.encode("world");
            start = start + file.getChannel().write(helloBuf, start);
            file.getChannel().write(worldBuf, start);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
