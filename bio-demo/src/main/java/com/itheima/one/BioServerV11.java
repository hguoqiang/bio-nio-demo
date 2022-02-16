package com.itheima.one;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 服务端
 * @author: huangguoqiang
 * @create: 2022-01-26 19:16
 **/
public class BioServerV11 {
    public static void main(String[] args) {

        try {
            //创建服务端对象,绑定端口
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(9999));
            System.out.println("========服务端启动=======");

            //等待客户端的连接，得到一个Socket对象,accept方法会阻塞，直到有客户端连接过来
            Socket socket = serverSocket.accept();
            System.out.println("有请求进入:"+socket.getPort());

            //从socket中得到一个字节输入流
            InputStream is = socket.getInputStream();

            //把字节输入流包装成一个字符输入流，按行读取
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line;
            //bufferedReader.readLine()这个方法也是阻塞，一直等待客户端发送数据，
            //如果第一个连接过来的客户端一直不发送数据，这个方法一直阻塞在这，导致其它客户端想连接到服务端却连接不过来
            if ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
