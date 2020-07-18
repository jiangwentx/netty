package com.atguigu.netty.dubborpc.publicinterface;

//接口，服务消费者和提供者都需要
public interface HelloService  {
    String hello(String msg);
}
