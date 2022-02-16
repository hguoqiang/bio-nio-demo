package com.itheima.one;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @description:客户端
 * @author: huangguoqiang
 * @create: 2022-01-26 19:29
 **/
public class BioClientV11 {
    public static void main(String[] args) {

        try {
            //创建客户端，请求与服务端的连接
            Socket socket = new Socket();
            System.out.println("======客户端启动===="+args[0]);
            socket.connect(new InetSocketAddress(9999));
            System.out.println("======客户端连接完成======="+args[0]);

            //从Socket通信管道中得到一个字节输出流。
            OutputStream outputStream = socket.getOutputStream();

            //把字节流改装成自己需要的流进行数据的发送
            PrintStream printStream = new PrintStream(outputStream);
            printStream.println("我是客户端" +args[0]);
            printStream.flush();

            System.out.println("======客户端发送完成======="+args[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
