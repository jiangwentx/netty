package com.atguigu.niozerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NewNIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(7001));

        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readcount = 0 ;
            while(-1!=readcount){
                try{
                    readcount = socketChannel.read(byteBuffer);
                }catch (Exception e){
                    e.printStackTrace();

                }
                byteBuffer.rewind(); //倒带          position = 0; mark = -1;

            }

        }

    }

}
