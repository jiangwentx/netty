package com.atguigu.netty.dubborpc.netty;

import com.atguigu.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       //接收到客户端的信息，并调用服务
        System.out.println("msg="+msg);
        //客户端调用服务器api时，我们可以定义一个协议
        //比如 每次发消息都必须以某个字符串开头 HelloService#hello#

        if(msg.toString().startsWith("HelloService#hello#")){

            //消息传对象进来，获取到调用接口和实现类，利用反射调用即可

            String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);

        }




    }
}
