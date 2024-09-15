package com.czx.demoj.nio.socketChannel;

import com.czx.demoj.nio.messageEdgeSolve.MessageEdgeUtils;
import com.czx.demoj.nio.utils.BufferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        // create selector to manage multiple channel
        Selector selector = Selector.open();
        // ByteBuffer buffer = ByteBuffer.allocate(16);

        try (ServerSocketChannel ssc = ServerSocketChannel.open()) {
            ssc.configureBlocking(false);

            // Used when event invoked to know which channel invoked event
            SelectionKey sscKey = ssc.register(selector, 0, null);

            // set interest (only interested in accept)
            sscKey.interestOps(SelectionKey.OP_ACCEPT);

            log.debug("register key: {}", sscKey);

            ssc.bind(new InetSocketAddress(8080));


            while (true) {
                // block the thread until interested event happens
                log.info("select...");
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                log.debug("现在维护了 {} 个 keys", selectionKeys.size());
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();

                    // 要删除，否则 selectionKeys 会越来越大，会充斥着没有事件的 channel
                    iterator.remove();

                    log.debug("key: {}", key);

                    // 处理连接
                    if (key.isAcceptable()) {
                        SocketChannel sc;
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        sc = channel.accept();
                        log.info("发生连接事件！连接处理！");
                        if (sc == null) {
                            log.info("没有发生连接事件！跳过连接处理！");
                            continue;
                        }
                        sc.configureBlocking(false);
                        SelectionKey register = sc.register(selector, 0, null);
                        register.interestOps(SelectionKey.OP_READ);
                        log.debug("accept: {}", sc);

                    }
                    // 处理消息
                    else if (key.isReadable()) {
                        log.debug("进入可读事件处理");
                        SocketChannel channel = (SocketChannel) key.channel();
                        try {
                            log.debug("进入消息边界处理");
                            ByteBuffer buffer = MessageEdgeUtils.handleSingleSocketMessage(channel);
                            if (buffer != null) {
                                log.debug("检测到可读事件！处理！");
                                BufferUtils.debugAll(buffer);
                                // 会移动 buffer 位置
                                String s = new String(String.valueOf(StandardCharsets.UTF_8.decode(buffer)));
                                log.debug("我要给它回复一个 reverse");
                                StringBuilder sb = new StringBuilder(s);
                                sb.reverse();
                                log.debug(sb.toString());
                                channel.write(StandardCharsets.UTF_8.encode(sb.toString()));
                                buffer.clear();
                            } else {
                                log.debug("一个 socket 正常断开");
                                key.cancel();
                            }
                        } catch (SocketException e) {
                            log.debug("一个 socket 异常断开");
                            key.cancel();

                        }
                    }
                }
            }
        }
    }
}
