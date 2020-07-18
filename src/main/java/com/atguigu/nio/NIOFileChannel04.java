package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * NIO使用channel中的transform或者transferto方法来复制文件，和03相比省去了bytebuffer，速度更快
 */
public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        //创建相关流
        FileInputStream fileInputStream = new FileInputStream("d:\\a.png");
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\a2.png");
        
        //获取各个流对应的filechannel

        FileChannel sourcechannel = fileInputStream.getChannel();
        FileChannel dstchannel = fileOutputStream.getChannel();

        //使用transform完成拷贝
        //dstchannel.transferFrom(sourcechannel,0,sourcechannel.size());

        sourcechannel.transferTo(0,sourcechannel.size(),dstchannel);

        //关闭相关的流
        sourcechannel.close();
        dstchannel.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}
