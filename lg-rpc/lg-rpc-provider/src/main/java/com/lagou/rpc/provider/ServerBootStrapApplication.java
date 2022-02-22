package com.lagou.rpc.provider;

import com.lagou.rpc.provider.server.NettyRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-22 15:15
 **/
@SpringBootApplication
public class ServerBootStrapApplication implements CommandLineRunner {

    @Autowired
    private NettyRpcServer nettyRpcServer;

    public static void main(String[] args) {
        SpringApplication.run(ServerBootStrapApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                nettyRpcServer.startServer("127.0.0.1", 8989);
            }
        }).start();
    }
}
