package com.lagou.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-19 18:10
 **/
public class MessageEncoder extends MessageToMessageEncoder {
    /**
     * 编码
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */


    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        System.out.println("正在进行消息编码");
        String s = (String) msg;
        out.add(Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
    }
}
