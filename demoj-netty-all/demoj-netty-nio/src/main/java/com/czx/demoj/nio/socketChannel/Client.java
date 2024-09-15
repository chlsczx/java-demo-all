package com.czx.demoj.nio.socketChannel;

import com.czx.demoj.nio.utils.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private static final Logger log = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        try (SocketChannel sc = SocketChannel.open()) {
            sc.connect(new InetSocketAddress("127.0.0.1", 8080));
            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(16);
            ByteBuffer receiveBuffer = ByteBuffer.allocate(16);
            sc.configureBlocking(false);
            boolean isLengthInput = true;
            while (scanner.hasNextLine()) {
                String s = scanner.nextLine();
                if (s.equals("exit")) {
                    sc.close();
                    break;
                }
                // if (isLengthInput) {
                if (s.length() <= 8) {
                    System.out.println("incorrect length");
                    continue;
                }
                ByteBuffer lenBuf = ByteBuffer.allocate(4);
                lenBuf.clear();
                for (int i = 0; i < 4; i += 1) {
                    Byte b = Byte.valueOf(s.substring(i * 2, i * 2 + 2), 16);
                    log.info("i: {}, s.substring(i * 2, i * 2 + 2): {}, valueOf: {}", i, s.substring(i * 2, i * 2 + 2), b);
                    lenBuf.put(b);
                }
                log.debug("put结束，写入长度");

                // 写 sc，需要读取 buf，因此需要 flip
                lenBuf.flip();
                BufferUtils.debugAll(lenBuf);
                sc.write(lenBuf);
                // isLengthInput = false;
                log.debug("写入结束，转换标志符");
                String msgStr = s.substring(8);
                ByteBuffer msgBuf = StandardCharsets.UTF_8.encode(msgStr);
                // if(msgBuf.capacity() != )

                // } else {
                log.debug("写入信息：{}", msgStr);
                sc.write(msgBuf);
                // isLengthInput = true;
                // }

                // while (true) {
                int read = sc.read(buffer);
                if (read > 0) {
                    System.out.println("哥哥上个信息有回复过来了哦");
                    BufferUtils.debugAll(buffer);
                    buffer.clear();
                }
                // }
            }
        }
        // System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
