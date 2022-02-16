package com.ithema.nio.chat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-16 15:02
 **/
public class Client {

    public static void main(String[] args) throws Exception {
        //获取客户端通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(9999));
        socketChannel.configureBlocking(false);


        //获取选择器
        Selector selector = Selector.open();
        //注册客户端到选择器上，监听从服务器发送到本客户端的事件
        socketChannel.register(selector, SelectionKey.OP_READ);


        //启动一个线程从服务器读取数据
        new Thread() {
            @Override
            public void run() {
                try {
                    while (selector.select() > 0) {
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isReadable()) {
                                SelectableChannel selectableChannel = key.channel();
                                if (selectableChannel instanceof SocketChannel) {
                                    SocketChannel channel = (SocketChannel) selectableChannel;
                                    ByteBuffer buffer = ByteBuffer.allocate(1024);

                                    StringBuilder msgBuilder = new StringBuilder();
                                    int len = 0;
                                    while ((len = channel.read(buffer)) > 0) {
                                        buffer.flip();
                                        msgBuilder.append(new String(buffer.array(), 0, len));
                                    }

                                    String msg = msgBuilder.toString();
                                    System.out.println(msg);


                                }
                            }
                            iterator.remove();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();


        //分配缓冲区大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //发送数据到服务器
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println(socketChannel.getLocalAddress() + " 说：");
            String str = scanner.nextLine();
            buffer.put(str.getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }


    }


}
