package com.lagou.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @description: Netty案例-群聊天室
 * 1. 编写一个 Netty 群聊系统，实现服务器端和客户端之间的数据简单通讯
 * 2. 实现多人群聊
 * 3. 服务器端：可以监测用户上线，离线，并实现消息转发功能
 * 4. 客户端：可以发送消息给其它所有用户，同时可以接受其它用户发送的消息
 * @author: huangguoqiang
 * @create: 2022-02-21 10:23
 **/
public class NettyChatServer {

    int port;

    public NettyChatServer(int port) {
        this.port = port;
    }

    public static void main(String[] args)  {
        NettyChatServer nettyChatServer = new NettyChatServer(9999);
        nettyChatServer.run();
    }

    public void run() {
        EventLoopGroup bossGroup = null;
        //创建workerGroup线程组，用于处理网络读写时间
        EventLoopGroup workerGroup = null;

        try {
            //创建bossGroup线程组，用于处理网络连接事件,构造器参数如果不指定线程数，默认是本地主机的cpu核心 * 2
            bossGroup = new NioEventLoopGroup(1);
            //创建workerGroup线程组，用于处理网络读写时间
            workerGroup = new NioEventLoopGroup();
            //创建服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //配置线程组
            serverBootstrap.group(bossGroup, workerGroup);
            //配置服务端通道
            serverBootstrap.channel(NioServerSocketChannel.class);
            //配置参数
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            //配置通道初始化器
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //添加解码器
                    socketChannel.pipeline().addLast(new StringDecoder());
                    //添加编码器
                    socketChannel.pipeline().addLast(new StringEncoder());
                    //添加处理器，实现聊天室业务
                    socketChannel.pipeline().addLast(new NettyChatServerHandler());

                }
            });

            //绑定端口启动服务器
            ChannelFuture channelFuture = serverBootstrap.bind(port);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("绑定端口成功");
                    } else {
                        System.out.println("绑定端口失败");
                    }
                }
            });

            System.out.println("聊天室服务器启动成功");

            //关闭通道(并不是真正意义上关闭,而是监听通道关闭的状态)和关闭连接池
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            //关闭通道
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }


}
