package com.itheima.three;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerRequestProcessor extends Thread {

    private Socket socket;
    private int clientPort;

    public ServerRequestProcessor(Socket socket) {
        this.socket = socket;
        this.clientPort = socket.getPort();
    }


    @Override
    public void run() {
        try {

            //从socket中得到一个字节输入流
            InputStream is = socket.getInputStream();

            //把字节输入流包装成一个字符输入流，按行读取
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line;
            //bufferedReader.readLine()这个方法也是阻塞，一直等待客户端发送数据，
            //如果第一个连接过来的客户端一直不发送数据，这个方法一直阻塞在这，导致其它客户端想连接到服务端却连接不过来
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("客户端" + clientPort + ":" + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}