package com.lagou.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-21 13:50
 **/
public class NettyChatClientHandler extends SimpleChannelInboundHandler<String> {


    /**
     * 通道读就绪事件，接收服务端发过来的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }





}
