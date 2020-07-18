package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty编程实战，socket服务
 */
public class NettyServer {
    public static void main(String[] args) throws Exception {

            //创建BossGroup和 workerGroup
            //1.创建两个线程组bossGroup和workerGroup
            //2.bossGroup只是处理连接请求，真正和客户端业务处理，会交给workGroup
            //3.两个都是无限循环
            //4.bossGroup，workGroup默认是cpu的核数 * 2
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workGroup = new NioEventLoopGroup();  //默认个数是cpu核数*2
             try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程的方式
            bootstrap.group(bossGroup, workGroup). //设置两个线程组
                    channel(NioServerSocketChannel.class). //使用NioServerSocketChannel类型作为服务器通道实现
                    option(ChannelOption.SO_BACKLOG, 128)  //设置线程队列连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)  //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {  //创建一个通道测试对象
                        //给pipeline设置处理器
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            System.out.println("客户channel的hashcode="+socketChannel.hashCode());

                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });   //给workGroup的EventLoop对应的管道设置处理器
            System.out.println("...服务器 is ready...");

            //绑定一个端口并且同步，生成一个ChannelFuture对象
            //启动服务器(并绑定端口）
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
        }

    }
}
