package com.lagou.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-19 16:58
 **/
public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    /**
     * 通道就绪事件,就可以发送数据了
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("你好，我是客户端");
    }


    /**
     * 通道读就绪事件，读取服务端发来的消息
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("读取服务端发来的消息, " + msg);
    }

}
