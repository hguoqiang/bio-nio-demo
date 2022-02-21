package com.lagou.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;


/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-21 13:40
 **/
public class NettyChatClient {


    private String ip;
    private int port;

    public NettyChatClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        NettyChatClient nettyChatClient = new NettyChatClient("127.0.0.1", 9999);
        nettyChatClient.run();
    }

    public void run() {
        EventLoopGroup group = null;
        try {

            //创建客户端线程组
            group = new NioEventLoopGroup();
            //创建客户端启动助手
            Bootstrap bootstrap = new Bootstrap();
            //配置线程组
            bootstrap.group(group);
            //配置客户端通道
            bootstrap.channel(NioSocketChannel.class);
            //配置通道初始化器
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    //解码器
                    ch.pipeline().addLast(new StringDecoder());
                    //编码器
                    ch.pipeline().addLast(new StringEncoder());

                    //添加客户端聊天室业务处理类
                    ch.pipeline().addLast(new NettyChatClientHandler());

                }
            });

            //启动客户端
            ChannelFuture channelFuture = bootstrap.connect(ip, port);

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println(future.channel().localAddress() + " 客户端连接成功");
                    } else {
                        System.out.println(future.channel().localAddress() + " 客户端连接失败");
                    }
                }
            });


            //发送消息给服务端
            Scanner scanner = new Scanner(System.in);
            while (true) {

                System.out.println("请输入");
                String msg = scanner.nextLine();
                if (msg.equals("exit")) {
                    break;
                }
                channelFuture.channel().writeAndFlush(msg);


            }

            channelFuture.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            group.shutdownGracefully();
        }


    }
}
