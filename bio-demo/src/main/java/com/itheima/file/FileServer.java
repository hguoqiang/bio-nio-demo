package com.itheima.file;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @description: 基于BIO形式下的文件上传
 * 目标:服务端支持接收任意类型文件，并且保存到服务端的磁盘上
 * @author: huangguoqiang
 * @create: 2022-01-27 11:19
 **/
public class FileServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("==========服务端启动==========");
            while (true){
                Socket socket = serverSocket.accept();
                new FileServerReadProcessor(socket).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
