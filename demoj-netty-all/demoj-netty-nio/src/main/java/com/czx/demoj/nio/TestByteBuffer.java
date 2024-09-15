package com.czx.demoj.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.czx.demoj.nio.utils.BufferUtils.getResourceFile;

public class TestByteBuffer {

    private static final Logger log = LoggerFactory.getLogger(TestByteBuffer.class);

    public static void main(String[] args) throws Exception {
        multipleTimesReadInBuffer();
    }

    public static void multipleTimesReadInBuffer() throws URISyntaxException {
        File file = getResourceFile("data.txt");
        try (FileChannel channel = new FileInputStream(file).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            int len;
            while (true) {
                len = channel.read(buffer);
                log.debug("Read {} bytes into the buffer.", len);
                if (len == -1) {
                    break;
                }

                buffer.flip();

                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    log.debug("{}", (char) b);
                }
                buffer.clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
