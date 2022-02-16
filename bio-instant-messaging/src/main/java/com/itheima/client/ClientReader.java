package com.itheima.client;

import com.itheima.util.ChatConstants;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-01-28 11:30
 **/
public class ClientReader extends Thread {
    private Socket socket;

    // 接收客户端界面，方便收到消息后，更新界面数据。
    private ClientChat client;

    public ClientReader(ClientChat client, Socket socket) {
        this.client = client;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            /** 循环一直等待客户端的消息 */
            while (true) {
                /** 读取当前的消息类型 ：登录,群发,私聊 , @消息 */
                int msgType = dis.readInt();
                switch (msgType) {
                    case ChatConstants
                            .LOGIN_MSG:
                        // 在线人数消息回来了
                        String nameDatas = dis.readUTF();
                        // 展示到在线人数的界面
                        String[] names = nameDatas.split(ChatConstants.SEPARATOR);

                        client.onLineUsers.setListData(names);
                        // 此处说明了如果启动客户端界面，以及登陆功能后，服务端收到新的登陆消息后，会响应一个在线列表用户回来给客户端更新在线人数！
                        break;

                    case ChatConstants
                            .PUBLIC_MSG:


                    case ChatConstants
                            .PRIVATE_MSG:

                        //群发,私聊 , @消息 都是直接显示的。
                        String msg = dis.readUTF();
                        client.smsContent.append(msg);
                        // 让消息界面滾動到底端
                        client.smsContent.setCaretPosition(client.smsContent.getText().length());

                        break;
                    default:
                        throw new IllegalArgumentException("unknown messageType");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
