package com.mini.rpc.core.util;

import com.mini.rpc.core.consumer.ConsumerInterceptor;
import com.mini.rpc.core.consumer.ConsumerInvocation;
import lombok.Data;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

/**
 * @Author dp
 * @Date 2024/3/8
 */
@Data
public class ProxyPlugin {


    public static Object buildJdkProxy(Class<?> itf) {
        return Proxy.newProxyInstance(
                itf.getClassLoader(),
                new Class[]{itf},
                new ConsumerInvocation(itf.getCanonicalName()));
    }


    public static Object buildCglibProxy(Class<?> claz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claz);
        enhancer.setCallback(new ConsumerInterceptor(claz.getCanonicalName()));
        return enhancer.create();
    }


}
