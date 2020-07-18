package com.atguigu.netty.GroupChart;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 明天客户端
 */
public class GroupChartServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channel组，管理所有channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //表示连接建立，一旦连接第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //遍历所有channel发送该消息
        channelGroup.writeAndFlush("客服端"+channel.remoteAddress()+"加入聊天");
        channelGroup.add(channel);

    }

    //离开推送给在线客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("客服端"+channel.remoteAddress()+"离开聊天");
        //channelGroup.remove(channel);
        System.out.println("channelGroup size="+channelGroup.size());
    }

    //表示channel处于活动状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+"离线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(ch->{
            if(ch!=channel){
                //其他人直接转发
                ch.writeAndFlush(sdf.format(new Date())+"  [客户]"+channel.remoteAddress()+"发送了消息:"+msg+"\n");
            }else{
                //自己
                ch.writeAndFlush(sdf.format(new Date())+"  [自己]"+channel.remoteAddress()+"发送了消息:"+msg+"\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
