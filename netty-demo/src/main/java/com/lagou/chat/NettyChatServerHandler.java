package com.lagou.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-21 13:19
 **/
public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static List<Channel> channels = new ArrayList<>();

    /**
     * 通道就绪事件 ，
     * 通过上下文得到连接的通道
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //当有新的客户端连接时，把客户端通道加入集合中
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println("server监听上线:" + channel.remoteAddress());
    }

    /**
     * 通道未就绪事件，通道下线事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //当有客户端通道下线时候，就移除
        channels.remove(channel);
        System.out.println("server监听下线:" + channel.remoteAddress());
    }

    /**
     * 通道读取事件,
     * 完成消息转发
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //得到当前发送消息的客户端通道
        Channel channel = ctx.channel();
        for (Channel target : channels) {
            if (target != channel) {
                target.writeAndFlush("[" + channel.remoteAddress() + "]说: " + msg);
            }
        }
    }

    /**
     * 异常处理事件
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        cause.printStackTrace();
        System.out.println("server监听异常:" + channel.remoteAddress());
    }
}
