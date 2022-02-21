package com.lagou.demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

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
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端发送过来的消息 byteBuf: " +byteBuf.toString(Charset.defaultCharset()));



    }


    /**
     * 通道读取完毕事件，可以给客户的做出响应
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = Unpooled.copiedBuffer("我是服务端", Charset.defaultCharset());
        //消息出站
        ctx.writeAndFlush(byteBuf);

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
