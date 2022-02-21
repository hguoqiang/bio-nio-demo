package com.lagou.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @description:自定义服务端handle
 * @author: huangguoqiang
 * @create: 2022-02-19 16:41
 **/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {



    /**
     * 通道读取事件
     *
     * @param ctx 通道上下文
     * @param msg 客户端发送过来的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端发送过来的消息 msg: " +msg);


    }


    /**
     * 通道读取完毕事件，可以给客户的做出响应
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //消息出站
        ctx.writeAndFlush("我是服务端");

    }


    /**
     * 通道异常事件
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
