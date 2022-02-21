package com.lagou.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-21 15:27
 **/
@Component
@ConfigurationProperties(prefix = "netty")
@Getter
@Setter
public class NettyConfig {
    //netty监听的端口
    private int port;
    //websocket的访问路径
    private String path;


}
