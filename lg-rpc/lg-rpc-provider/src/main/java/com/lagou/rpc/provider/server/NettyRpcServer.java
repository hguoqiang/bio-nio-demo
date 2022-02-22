package com.lagou.rpc.provider.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: netty 启动类
 * @author: huangguoqiang
 * @create: 2022-02-22 15:18
 **/
@Component
public class NettyRpcServer implements DisposableBean {

    @Autowired
    private NettyRpcServerHandler nettyRpcServerHandler;

    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;

    public void startServer(String ip, int port) {

        try {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup();

            ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup);

            serverBootstrap.channel(NioServerSocketChannel.class);

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new StringDecoder());
                    socketChannel.pipeline().addLast(new StringEncoder());

                    //加入自定义业务处理类
                    socketChannel.pipeline().addLast(nettyRpcServerHandler);


                }
            });


            ChannelFuture channelFuture = serverBootstrap.bind(ip, port).sync();
            System.out.println("netty rpc 服务端启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    @Override
    public void destroy() throws Exception {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
