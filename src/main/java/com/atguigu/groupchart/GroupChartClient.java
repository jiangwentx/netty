package com.atguigu.groupchart;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChartClient {

    //定义相关的属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;
    
    //构造器
    GroupChartClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        //设置非阻塞
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //得到username
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"is ok...");

    }

    //向服务器发送消息
    public void sendInfo(String info){
        info = username +"说:"+info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //读取从服务器回复的消息
    public void readInfo(){

        try {
            int readChannels = selector.select();

            if(readChannels>0){  //有事件发生
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){

                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        //得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        //读取
                        sc.read(byteBuffer);
                        //把读到的缓冲区数据转为字符串
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());

                    }else {
                        //System.out.println("没有可用的通道");
                        //continue;
                    }
                    //删除当前的selectionkey
                    iterator.remove();

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //启动客户端
        try {
            GroupChartClient chatClient = new GroupChartClient();
            //启动一个线程，每隔三秒，读取从服务器发来的数据
            new Thread(){
                public void run(){
                    while(true){
                        //读消息
                        chatClient.readInfo();
                        //暂停三秒
                        try {
                            Thread.currentThread().sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }.start();
            //发送数据
            Scanner scanner = new Scanner(System.in);
            //一旦检测到有输入，立刻发送
            while(scanner.hasNext()){
                String s = scanner.next();
                chatClient.sendInfo(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
