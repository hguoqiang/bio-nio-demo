package com.itheima.server;

import com.itheima.util.ChatConstants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: BIO模式下即时通信
 * 基于BIO模式下的即时通信，我们需要解决 客户端 到 客户端 的通信，也就是需要实现客户端与客户端的端口消息转发逻辑。
 * <p>
 * 服务端接收多个客户端逻辑
 * 目标
 * 服务端需要接收多个客户端的接入。
 * <p>
 * 实现步骤
 * 1.服务端需要接收多个客户端，目前我们采取的策略是一个客户端对应一个服务端线程。
 * 2.服务端除了要注册端口以外，还需要为每个客户端分配一个独立线程处理与之通信。
 * @author: huangguoqiang
 * @create: 2022-01-27 14:17
 **/
public class ServerChat {
    //定义一个map集合存放所有的在线的socket，key是 socket本身，value 是 用户名
    public static Map<Socket, String> onlineClients = new HashMap<>();

    public static void main(String[] args) {
        try {
            //创建服务端，注册端口
            ServerSocket serverSocket = new ServerSocket(ChatConstants.PORT);
            System.out.println("服务端启动");

            //循环，一直等待客户端的连接
            while (true) {
                Socket socket = serverSocket.accept();
                //为客户端分配一个独立的线程去处理与这个客户端的数据交互
                new ServerReader(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
