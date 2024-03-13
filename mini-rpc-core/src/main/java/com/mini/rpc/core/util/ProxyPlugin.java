package com.mini.rpc.core.util;

import com.mini.rpc.core.consumer.ConsumerInvocation;
import lombok.Data;

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

}
