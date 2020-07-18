package com.atguigu.netty.ihandlerandohandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

        System.out.println("服务起端IP为："+ctx.channel().remoteAddress()+"从服务端读到的数据为:"+msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端发送数据");
        ctx.writeAndFlush(123456789L);

        //ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcda", CharsetUtil.UTF_8));


    }
}
