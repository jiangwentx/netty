package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class NettyByteBuf02 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));


        if(byteBuf.hasArray()){
            byte[] content = byteBuf.array();
            System.out.println(new String(content,Charset.forName("utf-8")));
            System.out.println("byteBuf="+byteBuf);
        }

    }
}
