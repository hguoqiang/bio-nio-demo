package com.lagou.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.consumer.client.NettyRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @description:客户端代理类 1.创建代理对象
 * 2.封装RpcRequest对象
 * 3.创建RpcClient对象
 * 4.发送消息
 * 5.返回结果
 * @author: huangguoqiang
 * @create: 2022-02-22 18:43
 **/
public class RpcClientProxy {

    public static <T> T createProxy(Class<T> targetClass) {


        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{targetClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        //封装RpcRequest对象
                        RpcRequest rpcRequest = new RpcRequest();
                        rpcRequest.setRequestId(UUID.randomUUID().toString());
                        rpcRequest.setClassName(method.getDeclaringClass().getName());
                        rpcRequest.setMethodName(method.getName());
                        rpcRequest.setParameterTypes(method.getParameterTypes());
                        rpcRequest.setParameters(args);

                        //创建RpcClient对象
                        NettyRpcClient nettyRpcClient = new NettyRpcClient("127.0.0.1", 8989);

                        try {
                            //发送消息
                            Object rpcResponseResult = nettyRpcClient.send(JSON.toJSONString(rpcRequest));

                            //解析结果
                            RpcResponse rpcResponse = JSON.parseObject(rpcResponseResult.toString(), RpcResponse.class);

                            if (rpcResponse.getError() != null) {
                                throw new RuntimeException("远程调用异常：" + rpcResponse.getError());
                            }

                            Object result = rpcResponse.getResult();

                            return JSON.parseObject(result.toString(), method.getReturnType());
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        } finally {
                            nettyRpcClient.close();
                        }
                    }
                });
    }
}
