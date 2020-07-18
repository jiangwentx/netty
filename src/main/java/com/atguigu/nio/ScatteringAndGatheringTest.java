package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *Scattering:将数据写入到buffer，可以采取用buffer数组，聚合
 * Gattering:从buffer中读取数据时，会从buffer中依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        //使用serversocketchannel和socketchannel网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口至socket,并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8; //假定读取最大8
        //循环读取
        while(true){
            int byteRead = 0 ;

            while(byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead"+byteRead);

                 //使用流打印,javal1.8以上版本支持
                Arrays.asList(byteBuffers).stream().map(buffer->"positon="+buffer.position()+"limit="
                        +buffer.limit()).forEach(System.out::println);

                //将所有的buffer  flip
                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

                //将读到的数据回写到客户端
                long bytewrite = 0 ;
                while(bytewrite < messageLength){
                    long lw = socketChannel.write(byteBuffers);
                    bytewrite += lw;
                }

                //将所有的buffer 进行clear

                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

                System.out.println("byteRead"+byteRead +",byteWrite=" + bytewrite +"messageLength="+messageLength);

                Selector.open();

            }
        }


    }
}
