package com.atguigu.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

/**
 *TextWebSocketFrame 表示一个文本帧
 */
public class MyTestWebSocketFrameHanler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器端收到消息"+msg.text());

        //回复消息
        ctx.writeAndFlush(new TextWebSocketFrame("服务器时间"+ LocalDateTime.now()+"\n"+msg.text()));

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //asLongText唯一
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asLongText());
        //asShortText不唯一，有可能重复
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close(); //关闭连接
    }
}
