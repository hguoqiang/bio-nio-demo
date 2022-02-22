package com.lagou.rpc.consumer.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * @description: netty客户端
 * 1.连接netty服务端
 * 2.提供给调用者主动关闭资源的方法
 * 3.提供发送消息的方法
 * @author: huangguoqiang
 * @create: 2022-02-22 17:37
 **/
public class NettyRpcClient {

    private String ip;
    private int port;
    EventLoopGroup group = null;

    Channel channel = null;

    private NettyRpcClientHandler nettyRpcClientHandler = new NettyRpcClientHandler();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    public NettyRpcClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        init();
    }

    public void init() {
        try {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);

            bootstrap.channel(NioSocketChannel.class);

            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(new StringDecoder());

                    //添加自定义客户端处理类
                    pipeline.addLast(nettyRpcClientHandler);

                }
            });

            channel = bootstrap.connect(ip, port).sync().channel();

            System.out.println("netty 客户端启动成功");


        } catch (Exception e) {
            e.printStackTrace();

            if (channel != null) {
                channel.close();
            }


            if (group != null) {
                group.shutdownGracefully();
            }
        }

    }

    public void close() {
        if (channel != null) {
            channel.close();
        }


        if (group != null) {
            group.shutdownGracefully();
        }
    }


    public Object send(String requestMsg) throws Exception {
        nettyRpcClientHandler.setRequestMsg(requestMsg);
        Future submit = executorService.submit(nettyRpcClientHandler);

        return submit.get();
    }


}
