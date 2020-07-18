package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO将字符串写入本地文件
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str="hello,尚硅谷";
        //创建一个输出流->channel
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");

        //通过这个fileoutputstream 获取对应的 filechannel
        //这个filechannel真实类型是FileChannelImpl
        FileChannel filechannel = fileOutputStream.getChannel();

        //创建一个缓冲区ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        byteBuffer.put(str.getBytes());

        //读取缓冲器的字符到channel,此时要对bytebuffer反转，因为bytebuffer的position已经为末尾了
        byteBuffer.flip();

        //将byteBuffer写入到fileChannel
        filechannel.write(byteBuffer);
        filechannel.close();



    }

}
