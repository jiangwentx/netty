package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可让文件在内存(堆外内存)中直接修改，不需要拷贝
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {
        //MappedByteBuffer
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        /**
         * FileChannel.MapMode.READ_WRITE 读写模式
         *  0： 可以直接修改的起始位置
         *  5： 是映射到的内存大小， 即将1.txt的多少个字节映射到内存
         *  可以直接修改的范围 0-5
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0,(byte) 'H');
        mappedByteBuffer.put(3,(byte)'9');

        randomAccessFile.close();
    }
}
