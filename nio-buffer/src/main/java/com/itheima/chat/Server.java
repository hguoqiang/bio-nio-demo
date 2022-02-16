package com.itheima.chat;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @description: NIO 非阻塞式网络通信
 * @author: huangguoqiang
 * @create: 2022-02-16 10:41
 **/
public class Server {
    public static void main(String[] args) throws Exception {

        //1. 获取服务端通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //2.配置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //3. 如何让客户端去连接服务端，那就是绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9999));

        //4. 获取选择器
        Selector selector = Selector.open();

        //5.将通道注册到选择器,并且指定监听 接收 事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务端启动了");


        //6. 使用selector选择器去轮询看是否有接收事件

        while (selector.select() > 0) {
            //7. 获取selector中所有注册的通道中 已经就绪好的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            //8. 遍历这些准备好的事件
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                //9.判断具体是什么事件
                if (selectionKey.isAcceptable()) {
                    System.out.println("isAcceptable");
                    //10. 如果是接收事件，直接获取当前接入的客户端通道
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    //11. 配置为非阻塞式
                    socketChannel.configureBlocking(false);

                    //12. 将当前客户端通道也注册到选择器,监听这个客户端是否有数据写道服务端，对于服务端来说就是 读事件
                    socketChannel.register(selector, SelectionKey.OP_READ);

                } else if (selectionKey.isReadable()) {
                    //13.如果是读事件 获取当前选择器的读事件,获取读事件对应的客户端通道
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    //14.读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = socketChannel.read(buffer)) > 0) {
                        //切换模式
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, len));
                        //清除缓冲区数据,但是缓冲区中的数据依然存在，但是处于“被遗忘”状态 ,归位了
                        buffer.clear();

                    }


                }

                //处理完成当前事件后，要移除掉，否则会造成重复处理
                iterator.remove();


            }


        }


    }
}
