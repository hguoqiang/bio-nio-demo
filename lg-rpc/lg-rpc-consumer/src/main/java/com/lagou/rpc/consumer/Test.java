package com.lagou.rpc.consumer;

import com.lagou.rpc.api.IUserService;
import com.lagou.rpc.consumer.proxy.RpcClientProxy;
import com.lagou.rpc.pojo.User;

/**
 * @description:
 * @author: huangguoqiang
 * @create: 2022-02-22 19:03
 **/
public class Test {
    public static void main(String[] args) {
        IUserService userService = RpcClientProxy.createProxy(IUserService.class);

        User user = userService.getById(2);
        System.out.println(user);

    }
}
