package com.lagou.rpc.provider.server;

import com.alibaba.fastjson.JSON;
import com.lagou.rpc.common.RpcRequest;
import com.lagou.rpc.common.RpcResponse;
import com.lagou.rpc.provider.annotation.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: Rpc服务端处理类
 * 1.将标有@RpcService的bean进行缓存
 * 2.接收客户端请求
 * 3.根据传递过来的beanName从缓存中查找对应的bean
 * 4.解析请求中的方法名称、参数类型、参数信息
 * 5.反射调用bean的方法
 * 6.对客户端进行响应
 * @author: huangguoqiang
 * @create: 2022-02-22 15:25
 **/
@Component
@ChannelHandler.Sharable
public class NettyRpcServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    private static final Map<String, Object> RPC_SERVICE_INSTANCE_MAP = new ConcurrentHashMap<>();

    /**
     * 通道读就绪事件
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //接收客户端请求 将 msg 转成 RpcRequest对象
        RpcRequest rpcRequest = JSON.parseObject(msg, RpcRequest.class);

        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());

        try {
            rpcResponse.setResult(handle(rpcRequest));
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setError(e.getMessage());
        }

        //响应
        ctx.writeAndFlush(JSON.toJSONString(rpcResponse));

    }

    private Object handle(RpcRequest rpcRequest) throws InvocationTargetException {
        //查找bean
        Object bean = RPC_SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
        if (bean == null) {
            throw new RuntimeException("找不到服务:" + bean);
        }

        //解析参数
        //方法名
        String methodName = rpcRequest.getMethodName();
        //参数类型
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();

        //具体参数
        Object[] parameters = rpcRequest.getParameters();


        //CGlib反射调用
        Class<?> beanClass = bean.getClass();

        FastClass fastClass = FastClass.create(beanClass);
        FastMethod method = fastClass.getMethod(methodName, parameterTypes);
        return method.invoke(bean, parameters);


    }

    /**
     * 1.将标有@RpcService的bean进行缓存
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (beansWithAnnotation != null && beansWithAnnotation.size() > 0) {
            for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
                Object serviceBean = entry.getValue();
                if (serviceBean.getClass().getInterfaces().length == 0) {
                    throw new RuntimeException("服务必须实现接口");
                }

                //这个serviceBean有可能实现了多个接口，默认用第一个接口的名字作为key
                RPC_SERVICE_INSTANCE_MAP.put(serviceBean.getClass().getInterfaces()[0].getName(), serviceBean);

            }


        }

    }
}
