package com.itheima;

import java.io.*;
import java.net.Socket;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-01-27 13:26
 **/
public class ServerConnectionProcessor extends Thread {
    private Socket socket;
    private int socketPort;

    public ServerConnectionProcessor(Socket socket) {
        this.socket = socket;
        this.socketPort = socket.getPort();
    }

    @Override
    public void run() {
        System.out.println("接收到客户端socketPort：" + socketPort);
        try {
            InputStream inputStream = socket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = br.readLine()) != null) {
                sendMsgToAllClient("客户端" + socketPort + ":说" + line);
            }

        } catch (Exception e) {
            System.out.println("当前socket下线了");
            Server.set.remove(socket);
            e.printStackTrace();

        }

    }

    private void sendMsgToAllClient(String msg) throws IOException {

        for (Socket sock : Server.set) {
            OutputStream outputStream = sock.getOutputStream();
            PrintStream ps = new PrintStream(outputStream);
            ps.println(msg);
            ps.flush();
        }
    }
}
