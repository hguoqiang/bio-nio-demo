package com.itheima.file;

import java.io.*;
import java.net.Socket;

/**
 * @description:
 * 目标:客户端支持任意类型文件形式的上传。
 * @author: huangguoqiang
 * @create: 2022-01-27 11:19
 **/
public class FileClient {
    public static void main(String[] args) {
        try {
            //创建客户端，与服务端连接
            Socket socket = new Socket("127.0.0.1", 8888);
            System.out.println("====客户端启动======");

            //把字节输出流包装成数据输出流
            OutputStream outputStream = socket.getOutputStream();

            DataOutputStream dos = new DataOutputStream(outputStream);

            //先发送上传文件的后缀给服务端
            dos.writeUTF(".png");
            //再发送文件数据
            //先把文件读进来
            File file = new File("D:\\dev\\work\\test\\test-io\\大厂面试之IO模式详解资料\\文件\\java.png");
            InputStream is = new FileInputStream(file);

            byte[] buf = new byte[1024];
            int length = -1;
            while ((length = is.read(buf)) > 0) {
                dos.write(buf, 0, length);
            }
            dos.flush();

            //通知服务端说客户端的数据发送完成了
            socket.shutdownOutput();

            //Thread.sleep(10000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
