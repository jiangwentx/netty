package com.atguigu.netty.ihandlerandohandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("从客户端："+ctx.channel().remoteAddress()+"读取到的数据"+msg);

        System.out.println("向客户端回送数据");
        ctx.writeAndFlush(98765L);
        //ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcda", CharsetUtil.UTF_8));
    }

}
