package com.lagou.rpc.consumer.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * @description: 客户端处理类
 * 1.发送消息
 * 2.接收消息
 * @author: huangguoqiang
 * @create: 2022-02-22 18:27
 **/
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {

    private ChannelHandlerContext ctx;

    private String requestMsg;

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    private Object responseMsg;

    /**
     * 通道就绪事件，得到当前上下文 ChannelHandlerContext
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    /**
     * 通道读就绪事件
     *
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        responseMsg = msg;
        //唤醒等待的线程
        notify();
    }


    /**
     * 发送消息
     *
     * @return
     * @throws Exception
     */

    @Override
    public synchronized Object call() throws Exception {
        //发送消息
        ctx.writeAndFlush(requestMsg);

        //当前线程等待
        wait();
        return responseMsg;
    }
}
