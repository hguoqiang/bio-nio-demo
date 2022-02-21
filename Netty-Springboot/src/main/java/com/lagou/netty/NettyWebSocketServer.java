package com.lagou.netty;

import com.lagou.config.NettyConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @description: netty 服务器
 * @author: huangguoqiang
 * @create: 2022-02-21 16:23
 **/
@Component
public class NettyWebSocketServer implements Runnable {

    @Autowired
    private NettyConfig nettyConfig;
    @Autowired
    private WebSocketChannelInit webSocketChannelInit;

    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * 资源关闭，在容器关闭时销毁
     */
    @PreDestroy
    public void close() {

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void run() {

        try {


            //服务端启动助手
            ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup);

            //设置服务端通道
            serverBootstrap.channel(NioServerSocketChannel.class);

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));

            //通道初始化对象
            serverBootstrap.childHandler(webSocketChannelInit);

            ChannelFuture channelFuture = serverBootstrap.bind(nettyConfig.getPort());

            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("netty 服务端启动成功");
                    } else {
                        System.out.println("netty 服务端启动失败");
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
