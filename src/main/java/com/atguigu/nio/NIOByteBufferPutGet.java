package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * NIO的byteBuffer写入后读取时数据类型要和放入的顺序要一致，否则出现未知错误
 */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        byteBuffer.putInt(3);
        byteBuffer.putLong(3L);
        byteBuffer.putChar('c');
        byteBuffer.putShort((short)4);

        byteBuffer.flip();

        System.out.println();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());

        //返回一个只读的buffer
        ByteBuffer asReadOnlyBuffer = byteBuffer.asReadOnlyBuffer();


        asReadOnlyBuffer.put((byte)1);

    }
}
