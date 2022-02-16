package com.itheima.file;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-01-27 11:30
 **/
public class FileServerReadProcessor extends Thread {
    private Socket socket;
    private int socketPort;

    public FileServerReadProcessor(Socket socket) {
        this.socket = socket;
        this.socketPort = socket.getPort();
    }

    @Override
    public void run() {
        try {
            System.out.println("服务端接收到客户端socketPort：" + socketPort);

            //得到一个数据输入流用来读取客户端发送过来的数据
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            //先读取客户端上传的文件后缀名
            String suffix = dis.readUTF();

            //定义字节输出流，把读取客户端上传的文件,保存到磁盘上
            FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\dev\\work\\test\\test-io\\大厂面试之IO模式详解资料\\文件\\server\\"+ UUID.randomUUID() + suffix));

            //读取文件
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = dis.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();

            System.out.println("服务端保存文件完成");


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
