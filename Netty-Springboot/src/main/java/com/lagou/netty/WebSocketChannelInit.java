package com.lagou.netty;

import com.lagou.config.NettyConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-21 16:29
 **/
@Component
public class WebSocketChannelInit extends ChannelInitializer {


    @Autowired
    private NettyConfig nettyConfig;


    @Autowired
    private NettyWebSocketServerHandler nettyWebSocketServerHandler;


    @Override
    protected void initChannel(Channel channel) throws Exception {
        //添加对 Http 协议的支持
        channel.pipeline().addLast(new HttpServerCodec());

        //添加对 大数据流 的支持
        channel.pipeline().addLast(new ChunkedWriteHandler());

        //添加HttpObjectAggregator 作用是 将多个消息体转成 单一的Request或者Response，因为post请求由三部分组成，request line，request header，message body
        channel.pipeline().addLast(new HttpObjectAggregator(8000));

        //将http协议升级成ws协议，增加对websocket的支持
        channel.pipeline().addLast(new WebSocketServerProtocolHandler(nettyConfig.getPath()));

        //添加自定义的处理器
        channel.pipeline().addLast(nettyWebSocketServerHandler);
    }
}
