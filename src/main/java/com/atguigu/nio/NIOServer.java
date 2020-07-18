package com.atguigu.nio;

/*import com.sun.org.apache.xpath.internal.operations.String;*/

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {

        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        Selector selector = Selector.open();

        //绑定一个6666端口并监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //开启非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //把serverSocketChannel注册到 selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        System.out.println("selector注册的数量"+selector.keys().size());



        //循环等待客户端连接
        while(true){

            //等待一秒，如果没有事件返回
            if(selector.select(1000) == 0){
                System.out.println("服务器等待一秒，无连接");
                continue;
            }

            //1.有事件发生能获取到selectionkeys
            //2.根据selectionkeys获取到连接的channel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //使用迭代器遍历
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                //如果有新的连接请求
                System.out.println("selector"+selector.hashCode());
                if(key.isAcceptable()){
                    SocketChannel socket = serverSocketChannel.accept();
                    System.out.println("得到了一个连接 SocketChannel"+socket.hashCode());
                    //socket连接注册到selector
                    //关联到bytebuffer缓冲区
                    socket.configureBlocking(false);
                    socket.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("客户端连接后注册的selectorkey的数量"+selector.keys().size());
                    System.out.println("selectorkeys的事件数量为"+selectionKeys.size());

                }
                if(key.isReadable()){
                    //通用key反向获取到channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    System.out.println("连接产生写入操作");
                    //根据attachment方法得到buffer，得到与之关联的共享数据
                    ByteBuffer bytebuffer = (ByteBuffer) key.attachment();

                    channel.read(bytebuffer);
                    //System.out.println("from客户端"+ bytebuffer.array().toString());
                    System.out.println("from客户端"+ new String(bytebuffer.array()));
                }
                //移除掉key,否则多线程会重复操作
                keyIterator.remove();

            }


        }





    }
}
