package com.itheima.server;

import com.itheima.util.ChatConstants;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @description: 服务端接收登陆消息以及监测离线
 * <p>
 * 需要注意的是，服务端需要接收客户端的消息可能有很多种。
 * 分别是登陆消息，群聊消息，私聊消息 和@消息。
 * 这里需要约定如果客户端发送消息之前需要先发送消息的类型，类型我们使用信号值标志（1，2，3）。
 * 1代表接收的是登陆消息
 * 2代表群发 | @消息
 * 3代表了私聊消息
 * 服务端的线程中有异常校验机制，一旦发现客户端下线会在异常机制中处理，然后移除当前客户端用户，把最新的用户列表发回给全部客户端进行在线人数更新。
 * @author: huangguoqiang
 * @create: 2022-01-27 14:20
 **/
public class ServerReader extends Thread {
    private Socket client;
    private InetSocketAddress address;

    public ServerReader(Socket client) {
        this.client = client;
        this.address = (InetSocketAddress) client.getRemoteSocketAddress();
        System.out.println("客户端:" + this.address + "连接成功");
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        try {
            //获取客户端字节输入流，包装成数据输入流，读取客户端发送的消息
            //循环一直等待客户端发送的消息
            while (true) {
                InputStream inputStream = client.getInputStream();
                dis = new DataInputStream(inputStream);

                //读取当前的消息类型 ：登录,群发,私聊 , @消息
                int msgType = dis.readInt();

                //如果是登录消息
                if (msgType == ChatConstants.LOGIN_MSG) {
                    //先将当前登录的客户端socket存到在线人数的socket集合中
                    String loginName = dis.readUTF();
                    ServerChat.onlineClients.put(client, loginName);
                    System.out.println("客户端:" + loginName + "登录成功," + client);
                }

                writeMsg(msgType, dis);

            }

        } catch (SocketException se) {
            se.printStackTrace();
            System.out.println("--有人下线了--");
            // 从在线人数中将当前socket移出去
            ServerChat.onlineClients.remove(client);
            try {
                // 从新更新在线人数并发给所有客户端
                writeMsg(ChatConstants.LOGIN_MSG, dis);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void writeMsg(int msgType, DataInputStream dis) throws Exception {
        switch (msgType) {


            case ChatConstants.LOGIN_MSG:
                //通知所有客户端，xxx上线了
                //读取所有的在线人数发送给所有的客户端去更新在线人数列表 onlineNames = [波仔,zhangsan,波妞]
                Collection<String> onlineNames = ServerChat.onlineClients.values();
                String msg = onlineNames.toString();

                /** 将消息发送给所有的客户端 */
                sendMsgToAll(msgType, msg);

                break;
            case ChatConstants.PUBLIC_MSG:
                //群聊消息 、@消息 转发到所有的客户端
                String publicMsg = dis.readUTF();

                //发件人名字
                String name = ServerChat.onlineClients.get(client);

                /*
                最终的消息内容格式：
                张三 2021-01-02 13:34:32
                    在干嘛
                */

                sendMsgToAll(msgType, assembleMsg(publicMsg, name, ""));

                break;

            case ChatConstants.PRIVATE_MSG:
                //私聊消息处理
                /*
                最终的消息内容格式：
                张三 2021-01-02 13:34:32 对您私发
                    在干嘛
                */
                //读取消息
                String privateMsg = dis.readUTF();

                //收件人名字
                String receiver = dis.readUTF();

                //发件人名字
                String sendName = ServerChat.onlineClients.get(client);

                sendMsgToDestination(assembleMsg(privateMsg, sendName, "对您私发"), receiver);

                break;
            default:
                throw new IllegalStateException("Unexpected value: " + msgType);
        }

    }

    /**
     * @param msg
     * @param receiver 接收人
     */
    private void sendMsgToDestination(String msg, String receiver) {

        try {
            Set<Map.Entry<Socket, String>> entries = ServerChat.onlineClients.entrySet();
            for (Map.Entry<Socket, String> entry : entries) {

                if (entry.getValue().equals(receiver)) {
                    Socket client = entry.getKey();
                    DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                    dos.writeInt(ChatConstants.PRIVATE_MSG);
                    dos.writeUTF(msg);
                    dos.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String assembleMsg(String msg, String name, String tag) {
        // 时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EEE");

        StringBuilder finalMsg = new StringBuilder();
        finalMsg.append(name)
                .append("  ")
                .append(sdf.format(new Date()))
                .append(tag)
                .append("\r\n")
                .append("    ")
                .append(msg)
                .append("\r\n");
        return finalMsg.toString();
    }

    private void sendMsgToAll(int msgType, String msg) {
        try {
            // 拿到所有的在线socket管道 给这些管道写出消息
            Set<Map.Entry<Socket, String>> entries = ServerChat.onlineClients.entrySet();
            for (Map.Entry<Socket, String> entry : entries) {
                Socket client = entry.getKey();
                OutputStream outputStream = client.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputStream);
                dos.writeInt(msgType);
                dos.writeUTF(msg);
                dos.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
