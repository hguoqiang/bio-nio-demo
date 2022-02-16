package com.itheima;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @description: BIO模式下的端口转发思想
 * 需求：需要实现一个客户端的消息可以发送给所有的客户端去接收。（群聊实现）
 * 服务端实现
 * 1、创建服务端对象，注册端口
 * 2、接收客户端的连接，为每一个客户端分配一个线程去处理
 * 3、把当前连接的 socket 客户端 加入到一个在线的socket集合中
 * 4、接收当前的socket消息，然后推送给集合中的每一个socket
 * @author: huangguoqiang
 * @create: 2022-01-27 13:19
 **/
public class Server {
    public static Set<Socket> set = new LinkedHashSet<>();
    public static void main(String[] args) {
        try {

            ServerSocket serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = serverSocket.accept();
                set.add(socket);
                new ServerConnectionProcessor(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
