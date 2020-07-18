package com.atguigu.groupchart;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChartServer {

    private Selector selector;
    private ServerSocketChannel listenchannel;
    private static final int PORT = 6667;

    GroupChartServer(){
        try{
            //得到选择器
            selector = Selector.open();
            //ServerSocketChannel
            listenchannel = ServerSocketChannel.open();
            //绑定端口
            listenchannel.socket().bind(new InetSocketAddress(PORT));
            //设置为非阻塞
            listenchannel.configureBlocking(false);
            //listenchannel注册到selector
            listenchannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //监听方法
    public void listen(){

        System.out.println("监听线程："+Thread.currentThread().getName());

        try{
            //循环处理
            while(true){
                int count = selector.select();
                if(count>0){ //有事件发生了
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = listenchannel.accept();
                            socketChannel.configureBlocking(false);
                            //将该注册到selector
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println("地址"+socketChannel.getRemoteAddress()+"上线");

                        }
                        //通道是可读的状态
                        if(key.isReadable()){
                            readData(key);
                        }

                        //当前key删除
                        iterator.remove();
                    }


                }else{
                    System.out.println("等待中");
                }


            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 读取客户端消息
     */
    public void readData(SelectionKey key){
        //定义一个socketchannel
        SocketChannel channel = null;
        try{
            //取到关联的channel
            channel= (SocketChannel) key.channel();
            //创建缓冲buffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = channel.read(byteBuffer);
            //根据count值处理
            if(count>0){
                //把缓存区的数据转成字符串
                String msg = new String(byteBuffer.array());
                System.out.println("from 客户端" + msg);
                //向其它的客户端转发消息
                sendInfoToOtherClient(msg,channel);

            }

        }catch (IOException e){
            //如果发生异常
            try {
                System.out.println(channel.getRemoteAddress()+"离线...");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    //转发消息给其它客户端
    public void sendInfoToOtherClient(String msg,SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        System.out.println("服务器转发数据给客户端线程:"+Thread.currentThread().getName());
        //遍历所有注册到 selector上的socketchannel,并排除自身
        for(SelectionKey key:selector.keys()){
            //通过key,取出对应的 socketChannel
            Channel targetChannel = key.channel();

            //排除自己
            if(targetChannel instanceof SocketChannel
                    && targetChannel!=self){
                //转发给其他通道
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer数据写入通道
                dest.write(buffer);

            }

        }

    }

    public static void main(String[] args) {
        new GroupChartServer().listen();
    }
}
