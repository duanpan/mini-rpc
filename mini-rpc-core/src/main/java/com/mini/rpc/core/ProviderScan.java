package com.mini.rpc.core;

import com.mini.rpc.core.annotation.RpcProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 服务提供者扫描
 *
 * @Author dp
 * @Date 2024/3/7
 */
@Component
public class ProviderScan {

    @Autowired
    private ApplicationContext applicationContext;
    public static Map<String, ProviderInfo> providers = new HashMap();

    @PostConstruct
    public void initProvider() {
        Map<String, Object> providerBeans = applicationContext.getBeansWithAnnotation(RpcProvider.class);
        providerBeans.forEach((k, v) -> {
            Class<?>[] interfaces = v.getClass().getInterfaces();
            if (interfaces != null) {
                for (Class<?> itf : interfaces) {
                    Method[] methods = itf.getMethods();
                    if (methods == null) {
                        continue;
                    }

                    for (Method method : methods) {
                        ProviderInfo serviceInfo = new ProviderInfo();
                        serviceInfo.setService(v);
                        serviceInfo.setMethod(method);
                        String serviceName = RpcUtil.generateSericeSign(itf, method);
                        serviceInfo.setServiceName(serviceName);
                        providers.put(serviceName, serviceInfo);
                    }

                }
            }
        });
        System.out.println("--------");
    }

}
