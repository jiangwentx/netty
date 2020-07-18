package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用线程池来建立socket通信，属于BIO模式，使用telnet连接
 */
public class BIOServer {
    //线程池机制

    //思路：
    //1.创建一个线程池
    //2.如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
    public static void main(String[] args) throws IOException {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        //创建ServerSocket
        final ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        int count=0;
        while(true){
            System.out.println("线程信息 id="+Thread.currentThread().getId()+"线程名字"+Thread.currentThread().getName());
            System.out.println("等待连接...");
            //监听，等待客户端连接，一直等待新的客户端连接，会阻塞在这里
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程与之通讯
            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
            count++;
            System.out.println("执行次数"+count);

        }

    }


    //编写一个handler方法，和客户端通信
    //下面代码是将接收到socket获取inputstream内容转换为字符串输出
    public static void handler(Socket socket){

        System.out.println("线程信息 id="+Thread.currentThread().getId()+"线程名字"+Thread.currentThread().getName());
        byte[] bytes = new byte[1024];
        //通过socket,获取输入流
        try {
            InputStream inputStream = socket.getInputStream();
            //循环读取客户端发送的数据
            while(true){
                System.out.println("线程信息 id="+Thread.currentThread().getId()+"线程名字"+Thread.currentThread().getName());
                //一直等待数据，会阻塞在这里
                System.out.println("read...");
                int read = inputStream.read(bytes);
                if(read != -1){
                    //输出客户端发送的数据
                    System.out.println(new String(bytes,0,read));
                }else{
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
