package com.atguigu.netty.websocket;

import com.atguigu.netty.heartbeat.MyServerHanler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class Myserver {
    public static void main(String[] args) throws  Exception{
        {

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
                        //http编解码器
                        pipeline.addLast(new HttpServerCodec());
                        //块方式读写
                        pipeline.addLast(new ChunkedWriteHandler());
                        /**
                         * http分段发送的话，HttpObjectAggregator用来聚合
                         */
                        pipeline.addLast(new HttpObjectAggregator(8192));

                        /**
                         * 1.websocket是以frame传递的
                         * 2.webSocketFrame 有六个子类
                         * 3.浏览器请求ws://localhost:7000/hello
                         * 4.WebSocketServerProtocolHandler会将http协议升级为ws协议，保持长连接
                         */
                        pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));


                        //添加一个自定义的处理器
                        pipeline.addLast(new MyTestWebSocketFrameHanler());


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
}
