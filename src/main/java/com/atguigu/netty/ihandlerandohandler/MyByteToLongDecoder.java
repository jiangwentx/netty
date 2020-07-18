package com.atguigu.netty.ihandlerandohandler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {

    /**
     *
     * @param ctx 上下文对象
     * @param in 入站的bytebuf
     * @param out list集合，将解码后的数据传给下一个handler
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder 的decode方法被调用");
//        Long 8个字节
        if(in.readableBytes()>=8){
            out.add(in.readLong());
        }
    }
}
