package com.lagou.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

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
        ByteBuf byteBuf = Unpooled.copiedBuffer("你好，我是客户端", CharsetUtil.UTF_8);
        ctx.writeAndFlush(byteBuf);
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
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("读取服务端发来的消息, " + byteBuf.toString(CharsetUtil.UTF_8));
    }

}
