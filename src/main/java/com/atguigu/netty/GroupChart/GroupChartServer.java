package com.atguigu.netty.GroupChart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChartServer {
    private int port;

    public GroupChartServer(int port ){
        this.port = port;
    }

    public void run() {
        //创建两个线程组
        EventLoopGroup bossgroup = new NioEventLoopGroup(1);
        EventLoopGroup workergroup = new NioEventLoopGroup();
        try{
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossgroup,workergroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        
                        //获取到pipeline
                        ChannelPipeline pipeline = ch.pipeline();
                        //向pipeline加入解码器
                        pipeline.addLast("decoder",new StringDecoder());
                        //向pipeline加入编码器
                        pipeline.addLast("encoder",new StringEncoder());
                        //加入自己业务处理hander
                        pipeline.addLast(new GroupChartServerHandler());
                    }
                });

        System.out.println("netty服务起启动");
        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

        //关闭监听
        channelFuture.channel().closeFuture().sync();
        }catch (Exception e ){
            System.out.println(e);
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }

    }
    public static void main(String[] args) {
        new GroupChartServer(7000).run();
    }
}
