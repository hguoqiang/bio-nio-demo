package com.itheima.three;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 实现服务端可以接收多个客户端的请求，思路是服务端每接收到一个客户端的请求，就交由一个新的线程去处理与这个客户端的读写
 * @author: huangguoqiang
 * @create: 2022-01-26 19:16
 **/
public class BioServerV13 {


    public static void main(String[] args) {

        try {
            //创建服务端对象,绑定端口
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(9999));
            System.out.println("========服务端启动=======");

            //等待客户端的连接，得到一个Socket对象,accept方法会阻塞，直到有客户端连接过来
            //死循环，不断的接收客户端的请求
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("有请求进入,socket.getPort:" + socket.getPort());
                //创建一个新的线程来处理与这个客户端的读写
                ServerRequestProcessor serverRequestProcessor = new ServerRequestProcessor(socket);
                serverRequestProcessor.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
