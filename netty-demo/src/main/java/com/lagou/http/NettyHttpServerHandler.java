package com.lagou.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-21 14:44
 **/
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断是否是http请求
        if (msg instanceof HttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) msg;
            String uri = request.uri();
            System.out.println("客户端请求地址是：" + uri);
            if(uri.equals("/favicon.ico")){
                System.out.println("图标不响应");
                return;
            }

            //给浏览器客户端做出响应
            HttpVersion version = HttpVersion.HTTP_1_1;
            HttpResponseStatus status = HttpResponseStatus.OK;
            ByteBuf content = Unpooled.wrappedBuffer("Hello! 我是Netty服务器".getBytes());
            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(version, status, content);
            //设置响应头
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //响应
            ctx.writeAndFlush(defaultFullHttpResponse);
        }
    }
}
