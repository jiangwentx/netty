package com.atguigu.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;//上下文
    private String result;
    private String para;

    // 2
    void setPara(String para){
        System.out.println("setPara调用");
        this.para = para;
    }

    //被代理对象调用，发送数据给服务器 -> wait -> 等待被唤醒 ->返回结果        3 ->  5
    @Override
    public synchronized  Object call() throws Exception {
        System.out.println("call 被调用1");
        //发送参数给服务器
        context.writeAndFlush(para);
        //wait
        wait();
        System.out.println("call 被调用2");

        //等待channelRead唤醒，直到收到服务器的数据，拿到了result,返回即可
        return result;
    }

    //与服务器的连接创建后就会被调用         1
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive被调用");
        context= ctx;
    }

    //收到服务器的数据后，调用    4
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead被调用");
        System.out.println("服务器端返回结果为:"+msg.toString());
        result = msg.toString();
        //唤醒等待的线程
        notify();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
