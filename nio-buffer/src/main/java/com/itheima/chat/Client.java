package com.itheima.chat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-16 13:19
 **/
public class Client {

    public static void main(String[] args) throws Exception {
        //获取客户端通道
        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.connect(new InetSocketAddress(9999));

        //配置非阻塞模式
        socketChannel.configureBlocking(false);

        //分配缓冲区大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);


        //发送数据给服务端
        Scanner  scanner = new Scanner(System.in);

        while (true){
            System.out.println("请说：");
            String str = scanner.nextLine();
            buffer.put(str.getBytes());
            buffer.flip();

            //把数据写到通道
            socketChannel.write(buffer);

            //清除数据
            buffer.clear();

        }

    }
}
