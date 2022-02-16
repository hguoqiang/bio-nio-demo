package com.itheima.four;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 实现伪异步I/O的通信框架
 * 伪异步io采用了线程池实现，因此避免了为每个请求创建一个独立线程造成线程资源耗尽的问题，但由于底层依然是采用的同步阻塞模型，因此无法从根本上解决问题。
 * 如果单个消息处理的缓慢，或者服务器线程池中的全部线程都被阻塞，那么后续socket的i/o消息都将在队列中排队。新的Socket请求将被拒绝，客户端会发生大量连接超时。
 * @author: huangguoqiang
 * @create: 2022-01-26 21:05
 **/
public class BioServerV14 {
    public static void main(String[] args) {
        try {
            //创建服务端对象，绑定端口
            ServerSocket serverSocket = new ServerSocket(9999);

            System.out.println("服务端启动");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("有请求进入,socket.getPort:" + socket.getPort());
                ServerThreadPool.poolExecutor.execute(new ServerTask(socket));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
