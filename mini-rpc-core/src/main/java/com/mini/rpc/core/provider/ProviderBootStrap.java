package com.mini.rpc.core.provider;

import com.mini.rpc.core.util.RpcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 服务提供者扫描
 *
 * @Author dp
 * @Date 2024/3/7
 */
public class ProviderBootStrap {

    @Autowired
    private ApplicationContext applicationContext;

    public void providerStart() {
        Map<String, Object> providerBeans = applicationContext.getBeansWithAnnotation(RpcProvider.class);

        for (Object service : providerBeans.values()) {
            Class<?>[] interfaces = service.getClass().getInterfaces();
            if (interfaces == null) continue;

            for (Class<?> itf : interfaces) {
                Method[] methods = itf.getMethods();
                if (methods == null) continue;

                for (Method method : methods) {
                    RpcServiceInfo serviceInfo = new RpcServiceInfo();
                    serviceInfo.setServiceName(itf.getCanonicalName());
                    serviceInfo.setService(service);
                    serviceInfo.setMethodName(method.getName());
                    serviceInfo.setMethod(method);
                    serviceInfo.setArgsType(method.getParameterTypes());
                    String serviceSign = RpcUtil.buildServiceSign(serviceInfo);
                    ProviderCache.providers.put(serviceSign, serviceInfo);
                }
            }
        }

    }

}
