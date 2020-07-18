package com.atguigu.netty.dubborpc.netty;




import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.channels.ServerSocketChannel;

public class NettyServer {

    public static void startServer(String hostname,int port){
        startServer0(hostname,port);
    }

    public static void startServer0(String hostname,int port){
        EventLoopGroup workergroup = new NioEventLoopGroup();
        EventLoopGroup bossgroup = new NioEventLoopGroup();

        try{

            ServerBootstrap serverBootStrap = new ServerBootstrap();
            serverBootStrap.group(workergroup,bossgroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new NettyServerHandler());

                        }
                    });

            ChannelFuture channelFuture = serverBootStrap.bind(hostname, port).sync();
            System.out.println("服务提供方开始提供服务");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workergroup.shutdownGracefully();
            bossgroup.shutdownGracefully();
        }
    }
}
