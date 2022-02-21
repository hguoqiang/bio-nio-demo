package com.lagou.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description: netty 服务端
 * @author: huangguoqiang
 * @create: 2022-02-19 15:37
 **/
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //1. 创建bossGroup线程组: 处理网络事件--连接事件，如果不写线程数默认是 2 * 处理器线程数
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //2. 创建workerGroup线程组: 处理网络事件--读写事件，如果不写线程数默认是 2 * 处理器线程数
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3. 创建服务端启动助手
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //4. 设置bossGroup线程组和workerGroup线程组
        serverBootstrap.group(bossGroup, workerGroup);
        //5. 设置服务端通道实现为NIO
        serverBootstrap.channel(NioServerSocketChannel.class);
        //6. 参数设置
        //ChannelOption.SO_BACKLOG 用来初始化服务器可连接队列大小。服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接。
        // 多个客户 端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog 参数指定 了队列的大小。
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
        //ChannelOption.SO_KEEPALIVE ，一直保持连接活动状态。该参数用于设置TCP连接，当设置该选项以后，连接会测试链接的状态，这个选项用于可能长时间没有数据交流的连接。
        // 当设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        //7. 创建一个通道初始化对象
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //添加解码器
                socketChannel.pipeline().addLast("messageDecoder", new MessageDecoder());
                //添加编码器
                socketChannel.pipeline().addLast("messageEncoder", new MessageEncoder());
                //8. 向pipeline中添加自定义业务处理handler
                socketChannel.pipeline().addLast(new NettyServerHandler());
            }
        });


        //9. 启动服务端并绑定端口,同时将异步改为同步
        ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();

        System.out.println("服务端启动成功。。。。。。。。。。");
        //10. 关闭通道(并不是真正意义上的关闭，而是监听通道关闭的状态)和关闭连接池
        channelFuture.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }
}
