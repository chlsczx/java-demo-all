package com.czx.demoj.netty.echoServer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(EchoServerHandler.class);

    public static void main(String[] args) throws URISyntaxException, MalformedURLException {

URI uri = new URI("http://localhost:10808/aavbb/ccdd?a=1");
log.debug("uri scheme: {}\n" +
                " authority: {}\n" +
                " host: {}\n " +
        "user info: {}\n " +
                "fragment: {}\n " +
                "query: {}\n " +
        "path: {}\n ",
        uri.getScheme(),
        uri.getAuthority(),
        uri.getHost(),
        uri.getUserInfo(),
        uri.getFragment(),
        uri.getQuery(),
        uri.getPath()
);

        URL url = URL.of(uri, null);
        log.debug("url path: {}\n" +
                " Protocol: {}", url.getPath(), url.getProtocol());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        ByteBuf in = (ByteBuf) msg;
        log.debug("read: {}, write: {}, capacity: {}", in.readerIndex(), in.writerIndex(), in.capacity());
        while (in.readableBytes() > 0) {
            byte b = in.readByte();
            log.debug("byte: {}, char: {}", b, (char) b);
        }
        log.debug("read: {}, write: {}, capacity: {}", in.readerIndex(), in.writerIndex(), in.capacity());
        ctx.write(msg);
        ctx.flush();

        // log.debug("{}", in.capacity());
        // in.setIndex(0, in.capacity());
        // in.release();
        // try {
        //     while (in.isReadable()) {
        //         System.out.println((char) in.readByte());
        //         System.out.flush();
        //     }
        // } finally {
        //     // Discard the received data silently.
        //     ReferenceCountUtil.release(in);
        // }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
