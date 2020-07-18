package com.atguigu.netty.ihandlerandohandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder2 的decode方法被调用");
        //ReplayingDecoder不需要判断数据是否足够读取，内部会判断处理
//        Long 8个字节
//        if(in.readableBytes()>=8){
            out.add(in.readLong());
//        }
    }
}
