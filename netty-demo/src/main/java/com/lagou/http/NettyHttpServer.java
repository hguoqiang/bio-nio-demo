package com.lagou.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @description: netty 实现http服务器
 * 1. Netty 服务器在 8080 端口监听
 * 2. 浏览器发出请求 "http://localhost:8080/ "
 * 3. 服务器可以回复消息给客户端 "Hello! 我是Netty服务器 " ,并对特定请求资源进行过滤.
 * @author: huangguoqiang
 * @create: 2022-02-21 14:17
 **/
public class NettyHttpServer {

    private int port;

    public NettyHttpServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        new NettyHttpServer(8090).run();
    }

    public void run() {

        EventLoopGroup bossGroup = null;
        EventLoopGroup workerGroup = null;
        try {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //添加编解码器，处理http请求响应报文的
                    ch.pipeline().addLast(new HttpServerCodec());

                    //添加自定义http 请求 业务处理handler
                    ch.pipeline().addLast(new NettyHttpServerHandler());

                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(port);

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("http服务端启动成功");
                    } else {
                        System.out.println("http服务端启动失败");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
