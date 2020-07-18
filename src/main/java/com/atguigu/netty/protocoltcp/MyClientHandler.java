package com.atguigu.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("MyClientHandler异常信息="+cause.getMessage());
        ctx.close();
    }

    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        int len = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("客户端接收到的长度="+len);
        System.out.println("客户端接收到的消息=:"+new String(content,Charset.forName("utf-8")) +"字节数组="+content);

        System.out.println("客户端接收消息数量=" +(++this.count));


    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送10条 "今天天气冷，我要吃火锅"数据
        for (int i=0;i<5;i++){
            String msg = "今天天气冷，我要吃火锅";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int length = msg.getBytes(Charset.forName("utf-8")).length;
            MessageProtocol message = new MessageProtocol();
            message.setLen(length);
            message.setContent(content);

            ctx.writeAndFlush(message);

        }
    }
}
