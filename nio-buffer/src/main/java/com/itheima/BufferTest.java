package com.itheima;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-07 21:22
 **/
public class BufferTest {

    @Test
    public void test1(){
        //1. 分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("============allocate==============");
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.limit());//1024
        System.out.println(byteBuffer.capacity());//1024


        //2、使用put方法存入数据
        String str = "itheima";
        byteBuffer.put(str.getBytes());
        System.out.println("-----------------put()----------------");
        System.out.println(byteBuffer.position());//7
        System.out.println(byteBuffer.limit());//1024
        System.out.println(byteBuffer.capacity());//1024

        //3. 切换读取数据模式
        byteBuffer.flip();
        System.out.println("-----------------flip()----------------");
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.limit());//7
        System.out.println(byteBuffer.capacity());//1024


        //4. 利用 get() 读取缓冲区中的数据
        byte [] dst = new byte[byteBuffer.limit()];
        ByteBuffer byteBuffer1 = byteBuffer.get(dst);
        System.out.println(byteBuffer1==byteBuffer);//true
        System.out.println(new String(dst, 0, dst.length));
        System.out.println("-----------------dst()----------------");
        System.out.println(byteBuffer.position());//7
        System.out.println(byteBuffer.limit());//7
        System.out.println(byteBuffer.capacity());//1024



        //5. rewind() : 可重复读
        byteBuffer.rewind();
        System.out.println("-----------------rewind()----------------");
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.limit());//7
        System.out.println(byteBuffer.capacity());//1024

        //6. clear() : 清空缓冲区. 但是缓冲区中的数据依然存在，但是处于“被遗忘”状态
        byteBuffer.clear();
        System.out.println("-----------------clear()----------------");
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.limit());//1024
        System.out.println(byteBuffer.capacity());//1024
        System.out.println((char)byteBuffer.get());
        System.out.println(byteBuffer.hasRemaining());

    }


    @Test
    public void test2(){
        //1. 分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        System.out.println("============allocate==============");
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.limit());//10
        System.out.println(byteBuffer.capacity());//10


        //2、使用put方法存入数据
        String str = "itheima";
        byteBuffer.put(str.getBytes());
        System.out.println("============put==============");
        System.out.println(byteBuffer.position());//7
        System.out.println(byteBuffer.limit());//10
        System.out.println(byteBuffer.capacity());//10

        //3、切换读模式
        byteBuffer.flip();
        System.out.println("============flip==============");
        System.out.println(byteBuffer.position());//0
        System.out.println(byteBuffer.limit());//7
        System.out.println(byteBuffer.capacity());//10


        System.out.println("============get==============");
        //4、读数据
        byte [] b1 = new byte[2];
        byteBuffer.get(b1);

        System.out.println(new String(b1,0,b1.length));
        System.out.println(byteBuffer.position());//2
        System.out.println(byteBuffer.limit());//7
        System.out.println(byteBuffer.capacity());//10



        //5、标记当前位置
        byteBuffer.mark();
        System.out.println("============mark==============");
        byte [] b2 = new byte[3];
        byteBuffer.get(b2);

        System.out.println(new String(b2,0,b2.length));
        System.out.println(byteBuffer.position());//5
        System.out.println(byteBuffer.limit());//7
        System.out.println(byteBuffer.capacity());//10

        //6、回到标记位置，恢复到 mark 的位置
        byteBuffer.reset();
        System.out.println("============reset==============");
        System.out.println(byteBuffer.position());//2
        System.out.println(byteBuffer.limit());//7
        System.out.println(byteBuffer.capacity());//10

        if(byteBuffer.hasRemaining()){
            // remaining() 返回 position 和 limit 之间的元素个数
            System.out.println(byteBuffer.remaining());
        }



    }



    public static void main(String[] args) throws Exception {



    }



}
