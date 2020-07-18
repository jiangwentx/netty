package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * 说明
 * 1.SimpleChannelInboudhandler是ChannelInboundHandler的子类
 * 2.HttpObject 客户端和服务器端通讯数据封装对象
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject>
{

    //当有读取事件时，触发该函数
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断msg是不是http请求,       不同的协议封装不同对象，http,ftp,udp
        if(msg instanceof HttpRequest){

            System.out.println("perpiline hashcode="+ctx.pipeline().hashCode()+"TestHttpServerHandler hashcode="+this.hashCode());

            System.out.println("msg 类型="+msg);
            System.out.println("客户端地址"+ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            //特定的资源进行过滤
            System.out.println("uri.getPath()"+uri.getPath());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico，不做处理");
            }


            //回复信息给浏览器
            ByteBuf content = Unpooled.copiedBuffer("hello,我是服务器", CharsetUtil.UTF_8);

            //构造一个http的相应，即httpresponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            //文本形式
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            //长度
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //将构建的返回
            ctx.writeAndFlush(response);

        }


    }
}
