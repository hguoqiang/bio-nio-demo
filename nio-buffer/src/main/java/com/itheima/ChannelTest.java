package com.itheima;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-14 18:45
 **/
public class ChannelTest {

    //使用前面学习后的 ByteBuffer(缓冲) 和 FileChannel(通道)， 将 "hello,黑马Java程序员！" 写入到 data.txt 中.
    @Test
    public void test01() throws Exception {
        String str = "hello,黑马Java程序员！";

        //1、字节输出流通向目标文件
        FileOutputStream fos = new FileOutputStream(new File("data.txt"));

        //2、通字节输出流得到FileChannel
        FileChannel channel = fos.getChannel();


        //3、分配缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put(str.getBytes());


        //4、写数据到文件
        buffer.flip();//切换成写成模式
        channel.write(buffer);
        channel.close();
        System.out.println("------------------");


    }

    //使用前面学习后的 ByteBuffer(缓冲) 和 FileChannel(通道)， 将 data.txt 中的数据读入到程序，并显示在控制台屏幕
    @Test
    public void test02() throws Exception {
        FileInputStream is = new FileInputStream(new File("data.txt"));

        FileChannel channel = is.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //把数据扔到缓冲区了
        channel.read(buffer);

        buffer.flip();

        System.out.println(new String(buffer.array(), 0, buffer.remaining()));


    }


    @Test
    public void copy() throws Exception {
        File src = new File("8.png");

        File dst = new File("8new.png");

        FileInputStream is = new FileInputStream(src);

        FileOutputStream os = new FileOutputStream(dst);

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        FileChannel isChannel = is.getChannel();
        FileChannel osChannel = os.getChannel();

        while (true) {
            //首先情况缓冲区，然后再把数据读到缓冲区
            buffer.clear();

            //读
            int flag = isChannel.read(buffer);
            if (flag == -1) {
                break;
            }

            //切换模式
            buffer.flip();

            //把数据写出
            osChannel.write(buffer);

        }

        osChannel.close();
        isChannel.close();


    }


    /**
     * 分散 (Scatter) 和聚集 (Gather)
     * <p>
     * 分散读取（Scatter ）:是指把Channel通道的数据读入到多个缓冲区中去
     * <p>
     * 聚集写入（Gathering ）是指将多个 Buffer 中的数据“聚集”到 Channel。
     */
    @Test
    public void test03() throws Exception{
        FileInputStream is = new FileInputStream(new File("data.txt"));

        FileChannel isChannel = is.getChannel();
        ByteBuffer b1 = ByteBuffer.allocate(5);
        ByteBuffer b2 = ByteBuffer.allocate(102);
        ByteBuffer [] dsts = {b1,b2};
        //分散读取
        isChannel.read(dsts);

        //切换模式
        for (ByteBuffer dst : dsts) {
            dst.flip();
            System.out.println(new String(dst.array(), 0, dst.remaining()));
        }

        FileOutputStream os = new FileOutputStream(new File("data01.txt"));

        //聚集写入
        FileChannel osChannel = os.getChannel();
        osChannel.write(dsts);

        os.close();
        is.close();
    }

    @Test
    public void test04() throws Exception {
        // 1、字节输入管道
        FileInputStream is = new FileInputStream("data01.txt");
        FileChannel isChannel = is.getChannel();
        // 2、字节输出流管道
        FileOutputStream fos = new FileOutputStream("data03.txt");
        FileChannel osChannel = fos.getChannel();
        // 3、复制
        osChannel.transferFrom(isChannel,isChannel.position(),isChannel.size());
        isChannel.close();
        osChannel.close();
    }


    @Test
    public void test05() throws Exception {
        // 1、字节输入管道
        FileInputStream is = new FileInputStream("data01.txt");
        FileChannel isChannel = is.getChannel();
        // 2、字节输出流管道
        FileOutputStream fos = new FileOutputStream("data04.txt");
        FileChannel osChannel = fos.getChannel();
        // 3、复制
        isChannel.transferTo(isChannel.position() , isChannel.size() , osChannel);
        isChannel.close();
        osChannel.close();
    }
}
