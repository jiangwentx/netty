package com.atguigu.netty.dubborpc.customer;

import com.atguigu.netty.dubborpc.netty.NettyClient;
import com.atguigu.netty.dubborpc.publicinterface.HelloService;

public class ClientBootStrap {
    public static final String providerName= "HelloService#hello#";

    public static void main(String[] args) {
        NettyClient customer = new NettyClient();

        HelloService helloService = (HelloService) customer.getBean(HelloService.class, providerName);

        //通过代理对象调用服务提供者的方法(服务)
        for (int i=0;i<10;i++){
            String res = helloService.hello("你好，dubbo");

            System.out.println("调用的结果 res= "+res);
        }

    }
}
