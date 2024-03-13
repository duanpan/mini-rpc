package com.mini.rpc.core.consumer;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author dp
 * @Date 2024/3/12
 */
public class ConsumerInterceptor implements MethodInterceptor {

    private String serviceName;

    public ConsumerInterceptor(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return null;
    }
}
