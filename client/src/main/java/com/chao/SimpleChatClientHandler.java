package com.chao;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 客户端 channel
 *
 * @author waylau.com
 * @date 2015-2-26
 */
public class SimpleChatClientHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public static Channel channel;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception { // (1)
        Channel incoming = channelHandlerContext.channel();
        System.out.println("channelRead0");
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
        channel = ctx.channel();

        // Broadcast a message to multiple Channels
        channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + channel.remoteAddress() + " 加入"));
        System.out.println("Client:" + channel.remoteAddress() + "加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
        Channel incoming = ctx.channel();

        // Broadcast a message to multiple Channels
        channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));

        System.out.println("Client:" + incoming.remoteAddress() + "离开");

        // A closed Channel is automatically removed from ChannelGroup,
        // so there is no need to do "channels.remove(ctx.channel());"
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)    // (7)
            throws Exception {
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    public static void sendMessage(String string) {
        URI uri = null;
        try {
            uri = new URI("http://localhost:8080/server/websocket");
            DefaultFullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(),
                    Unpooled.wrappedBuffer(string.getBytes("UTF-8")));
            channel.writeAndFlush(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
