package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 1.自定义一个handler 需要继承netty规定好的某个handlerAdapter
 * 2.这时自定义一个Handler, 才能称handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    //读取数据事件

    /**
     * 1.channelHandlerContext ctx:上下文对象，含有管道pipeline,通道channel，地址
     * 2.ctx,  msg 客户端发送的数据，默认Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //1.用户自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            /*@Override*/
            public void run() {
                //如果这里有一个耗费时间比较长的任务->异步执行 ->提交该channel对应的
                //NIOEventLoop的taskQueue中，
                try {
                    Thread.sleep(10*1000);
                    System.out.println("测试");
                } catch (InterruptedException e) {
                    System.out.println("发生异常");
                    e.printStackTrace();
                }
                //ctx.writeAndFlush(Unpooled.copiedBuffer("hello客户端执行完毕",CharsetUtil.UTF_8));


            }
        });

        ctx.channel().eventLoop().execute(new Runnable() {
            /*@Override*/
            public void run() {
                //如果这里有一个耗费时间比较长的任务->异步执行 ->提交该channel对应的
                //NIOEventLoop的taskQueue中，
                try {
                    Thread.sleep(20*1000);
                    System.out.println("测试112");
                } catch (InterruptedException e) {
                    System.out.println("发生异常");
                    e.printStackTrace();
                }
                //ctx.writeAndFlush(Unpooled.copiedBuffer("hello客户端执行完毕",CharsetUtil.UTF_8));


            }
        });

        //用户自定义定时任务，该任务是提交到scheduleTaskQueue,延时五秒执行
        ctx.channel().eventLoop().schedule(new Runnable() {
            /*@Override*/
            public void run() {
                try {
                    Thread.sleep(20*1000);
                    System.out.println("测试112");
                } catch (InterruptedException e) {
                    System.out.println("发生异常");
                    e.printStackTrace();
                }
            }
        },5, TimeUnit.SECONDS);






        System.out.println("go on..");



        System.out.println("服务器读取线程"+Thread.currentThread().getName());
        System.out.println("server ctx=" + ctx);
        System.out.println("看看channel和pipeline的关系");
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = ctx.pipeline();  //底层是一个双向链表，涉及到一个出站和入站问题

        //将msg转成一个ByteBuf,
        // netty提供的，不是NIO的ByteBuffer
        ByteBuf buf = (ByteBuf)msg;
        System.out.println("客户端发送消息是"+buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址:"+ channel.remoteAddress());

    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓冲并刷新
        //一般讲，我们对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello客户端",CharsetUtil.UTF_8));

    }
    //处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
