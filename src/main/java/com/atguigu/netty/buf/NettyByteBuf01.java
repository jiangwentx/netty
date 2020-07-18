package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf01 {
    public static void main(String[] args) {

        //创建一个ByteBuf
        //1.创建一个对象，该对象包含一个数组arr,是一个byte[10]
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i=0;i<10;i++){
            buffer.writeByte(i);
        }

        System.out.println(buffer.capacity());

        for(int i=0;i<buffer.capacity();i++){
            System.out.println(buffer.getByte(i));
        }

    }
}
