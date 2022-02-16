package com.ithema.nio.chat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @description: 需求:进一步理解 NIO 非阻塞网络编程机制，实现多人群聊
 * <p>
 * 编写一个 NIO 群聊系统，实现客户端与客户端的通信需求（非阻塞）
 * 服务器端：可以监测用户上线，离线，并实现消息转发功能
 * 客户端：通过 channel 可以无阻塞发送消息给其它所有客户端用户，同时可以接受其它客户端用户通过服务端转发来的消息
 * @author: huangguoqiang
 * @create: 2022-02-16 14:12
 **/
public class Server {


    public static void main(String[] args) throws Exception {
        //获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //配置服务端通道为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //获取选择器
        Selector selector = Selector.open();

        //服务端通道注册到选择器
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        //循环是否有事件发生
        while (selector.select() > 0) {

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //判断是什么事件
                if (key.isAcceptable()) {
                    SocketChannel clientChannel = serverSocketChannel.accept();

                    clientChannel.configureBlocking(false);

                    clientChannel.register(selector, SelectionKey.OP_READ);

                    System.out.println(clientChannel.getRemoteAddress() + " 接入到服务器");

                } else if (key.isReadable()) {
                    SelectableChannel selectableChannel = key.channel();
                    if (selectableChannel instanceof SocketChannel) {
                        SocketChannel client = (SocketChannel) selectableChannel;
                        System.out.println(client.getRemoteAddress() + " 数据进入");

                        ByteBuffer buffer = ByteBuffer.allocate(3);

                        StringBuilder msgBuilder = new StringBuilder();
                        msgBuilder.append(client.getRemoteAddress() + " 说：");
                        int len = 0;
                        while ((len = client.read(buffer)) > 0) {
                            //切换模式
                            buffer.flip();

                            msgBuilder.append(new String(buffer.array(), 0, len));
                        }
                        String msg = msgBuilder.toString();
                        System.out.println(msg);

                        //遍历 所有注册到 selector 上的 SocketChannel,并排除当前客户端,把数据发送给所有的客户端
                        for (SelectionKey selectionKey : selector.keys()) {
                            SelectableChannel channel = selectionKey.channel();
                            if (channel instanceof SocketChannel && channel != client) {
                                SocketChannel target = (SocketChannel) channel;
                                //把消息封装成自己数组
                                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                                //将buffer 的数据写入 通道
                                target.write(byteBuffer);
                            }
                        }

                    }

                }

                //删除这个处理过色事件
                iterator.remove();
            }


        }
    }
}