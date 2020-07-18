package com.atguigu.bio;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ViewBIOServer {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            final ServerSocket serverSocket = new ServerSocket(6666);

            while(true){

            final Socket socket = serverSocket.accept();
            System.out.println("线程信息 id="+Thread.currentThread().getId()+"线程名字"+Thread.currentThread().getName());
            executorService.execute(new Runnable() {
                    public void run() {
                        System.out.println("创建一个线程");
                        hanlder(socket);
                    }
                });
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    static void hanlder(Socket socket){
        byte[] bytes = new byte[1024];
        try {
            InputStream inputStream = socket.getInputStream();

            while(true){
                System.out.println("线程信息 id="+Thread.currentThread().getId()+"线程名字"+Thread.currentThread().getName());
                int read = inputStream.read(bytes);
                if(read!=-1){
                    System.out.println("准备输出");
                    System.out.println(new String(bytes,0,read));
                }else{
                    break;
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
