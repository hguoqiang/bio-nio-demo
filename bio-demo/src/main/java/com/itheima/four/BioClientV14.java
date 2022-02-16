package com.itheima.four;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * @description:客户端
 * @author: huangguoqiang
 * @create: 2022-01-26 19:29
 **/
public class BioClientV14 {
    public static void main(String[] args) {

        try {
            //创建客户端，请求与服务端的连接
            Socket socket = new Socket();
            System.out.println("======客户端启动====");
            socket.connect(new InetSocketAddress(9999));
            System.out.println("======客户端连接完成,=======socket.getLocalPort:"+socket.getLocalPort());

            //从Socket通信管道中得到一个字节输出流。
            OutputStream outputStream = socket.getOutputStream();

            //把字节流改装成自己需要的流进行数据的发送
            PrintStream printStream = new PrintStream(outputStream);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("请输入：");
                String msg = scanner.nextLine();
                printStream.println(msg);
                printStream.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
