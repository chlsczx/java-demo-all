package com.czx.demoj.nio;

import com.czx.demoj.nio.utils.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class ChannelTransferTo {
    private static final Logger log = LoggerFactory.getLogger(ChannelTransferTo.class);

    public static void main(String[] args) throws Exception {
        File dataFile = BufferUtils.getResourceFile("test.mp4");
        Objects.requireNonNull(dataFile);
        try (RandomAccessFile raf = new RandomAccessFile("scattering_write.mp4", "rw");
             FileInputStream dataFileIs = new FileInputStream(dataFile);
             FileChannel dataFileChannel = dataFileIs.getChannel();) {

            FileChannel rafChannel = raf.getChannel();
            rafChannel.position(rafChannel.size());
            rafChannel.force(true);

            long size = dataFileChannel.size();
            long acc = 0;
            for (; size > 0; size = dataFileChannel.size() - acc) {
                log.info("remains {} bytes", size);
                // 零拷贝
                acc += dataFileChannel.transferTo(acc, Math.max((long) Math.pow(2, 24), size), rafChannel);
                log.info("transferred {} bytes", acc);
            }
        }
    }
}
