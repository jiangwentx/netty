package com.atguigu.nio;

import java.nio.Buffer;
import java.nio.IntBuffer;

public class Basicbuffer {
    //举例说明NIO中buffer的使用(简单案例)

    public static void main(String[] args) {
        //创建一个Buffer,大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);
        //向buffer 存放数据
        for(int i=0;i<intBuffer.capacity();i++){
            intBuffer.put(i*2);
        }

        //从buffer读取数据
        //将buffer转换，读写切换
        intBuffer.flip();
        intBuffer.position(1);
        intBuffer.limit(3);

        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }



    }
}
