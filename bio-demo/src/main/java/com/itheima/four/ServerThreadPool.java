package com.itheima.four;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-01-26 21:09
 **/
public class ServerThreadPool {


    public static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3, 10, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

}
