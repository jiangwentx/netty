package com.atguigu.netty.dubborpc.provider;

import com.atguigu.netty.dubborpc.netty.NettyServer;

//启动一个服务提供者 就是NettyServer
public class ServerBootStra {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",7000);

    }
}
