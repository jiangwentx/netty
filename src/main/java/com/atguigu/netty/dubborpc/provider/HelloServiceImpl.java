package com.atguigu.netty.dubborpc.provider;

import com.atguigu.netty.dubborpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {

    static int count=0;
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息"+msg);
        if(msg!=null){
            return "客户端我已经收到你的消息["+msg+"]+次数="+(++this.count);
        }else{
           return "客户端我已经收到你的消息";
        }

    }

}
