package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO使用Channel和byteBuffer复制文件
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {

        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel channel01 = fileInputStream.getChannel();
        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel channel02 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);


        while(true){
            //重要操作，重置缓冲区
            byteBuffer.clear();
/*            public final Buffer clear() {
                position = 0;
                limit = capacity;
                mark = -1;
                return this;
            }*/

            int read = channel01.read(byteBuffer);
            System.out.println("read="+read);
            if(read==-1){
                break;
            }
            byteBuffer.flip();
            //将byteBuffer写入到channel02中
            channel02.write(byteBuffer);

        }

        fileInputStream.close();
        fileOutputStream.close();



    }
}


/*  https://www.cnblogs.com/zq-boke/p/8523710.html
    传统的java io流复制，没有用到channel和byteBuffer
    private static void copyFileUsingFileStreams(File source, File dest)
            throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) ！= -1) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

    */
