package com.atguigu.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {

        int length = msg.getLen();
        byte[] content = msg.getContent();
        System.out.println("服务端接收信息如下");
        System.out.println("长度="+length);
        System.out.println("内容："+new String(content,Charset.forName("utf-8")));

        System.out.println("服务端接收到的数据量"+ (++this.count));

        String responseContent = UUID.randomUUID().toString();
        int responselen = responseContent.getBytes("utf-8").length;
        byte[] responsecontent = responseContent.getBytes("utf-8");
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(responselen);
        messageProtocol.setContent(responsecontent);

        ctx.writeAndFlush(messageProtocol);



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
