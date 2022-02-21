package com.lagou.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-19 17:59
 **/
public class MessageDecoder extends MessageToMessageDecoder {

    /**
     * 解码
     *
     * @param ctx
     * @param msg 消息
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {

        System.out.println("正在进行消息解密");
        ByteBuf byteBuf = (ByteBuf) msg;

        //把消息传递给下一个handler
        out.add(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
