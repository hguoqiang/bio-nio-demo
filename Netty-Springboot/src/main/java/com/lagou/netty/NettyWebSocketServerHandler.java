package com.lagou.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: websocket 处理类
 * <p>
 * TextWebSocketFrame :websocket的数据是以帧的形式处理
 * @author: huangguoqiang
 * @create: 2022-02-21 16:29
 **/
@Component
@ChannelHandler.Sharable
public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static List<Channel> channels = new ArrayList<>();

    /**
     * 通道就绪事件，监控通道上线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println("[Netty Server] 监控 " + channel.remoteAddress().toString().substring(1) + " 上线");
    }

    /**
     * 通道未就绪事件，监控通道下线
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(channel);
        System.out.println("[Netty Server] 监控 " + channel.remoteAddress().toString().substring(1) + " 下线");
    }

    /**
     * 读就绪事件，读取客户端发来的消息，转发给其它通道
     *
     * @param channelHandlerContext
     * @param textWebSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

        //获取消息
        String text = textWebSocketFrame.text();
        System.out.println(text);

        //获取到当前发送消息的客户端通道
        Channel channel = channelHandlerContext.channel();

        //转发给其它客户端
        for (Channel target : channels) {
            if (target != channel) {
                target.writeAndFlush(new TextWebSocketFrame(text));
            }
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();

        System.out.println("[Netty Server] 监控 " + ctx.channel().remoteAddress().toString().substring(1) + " 异常了");
    }
}
