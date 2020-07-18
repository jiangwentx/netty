package com.atguigu.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup bossgroup = new NioEventLoopGroup(1);
        EventLoopGroup workergroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossgroup,workergroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            /*
                            1.IdleStateHandler netty提供的处理器
                            2.long readerIdleTime 表示多长时间没读，就会发送一个心跳检测包检测其是否连接
                            3.long writerIdleTime, 表示多长时间没写，就会发送一个心跳检测包检测其是否连接
                            4.long allIdleTime,表示多长时间没读写，就会发送一个心跳检测包检测其是否连接
                            5.文档说明 Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
  read, write, or both operation for a while.

                            6.IdleStateHandler触发后会传递给下一个handler的userEventTigered处理
                             */

                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            //加入一个对空闲检测进一步处理的handler
                            pipeline.addLast(new MyServerHanler());

                        }
                    });

                ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
                channelFuture.channel().closeFuture().sync();

        }finally {
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
    }
}
