package com.mini.rpc.core.start;

import com.mini.rpc.core.provider.ProviderCache;
import com.mini.rpc.core.annotation.RpcProvider;
import com.mini.rpc.core.entity.ProviderMeta;
import com.mini.rpc.core.entity.ServiceMeta;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.helper.MetaBuildHelper;
import com.mini.rpc.core.util.RpcUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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
    @Autowired
    private RegistryCenter registryCenter;
    @Autowired
    private MetaBuildHelper rpcBuildHelper;


    public void start() {
        providerScan();
        providerRegister();

    }

    @SneakyThrows
    public void stop() {
        ProviderCache.providers.forEach((k, v) -> {
            ServiceMeta serviceInstance = rpcBuildHelper.buildServiceMeta(k);
            registryCenter.unRegister(serviceInstance);
        });

    }

    private void providerScan() {
        Map<String, Object> providerBeans = applicationContext.getBeansWithAnnotation(RpcProvider.class);

        for (Object service : providerBeans.values()) {
            Class<?>[] interfaces = service.getClass().getInterfaces();
            if (interfaces == null) continue;

            for (Class<?> itf : interfaces) {
                Method[] methods = itf.getMethods();
                if (methods == null) continue;

                for (Method method : methods) {
                    ProviderMeta serviceInfo = new ProviderMeta();
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

    @SneakyThrows
    private void providerRegister() {
        ProviderCache.providers.forEach((k, v) -> {
            ServiceMeta serviceMeta = rpcBuildHelper.buildServiceMeta(k);
            registryCenter.register(serviceMeta);
        });
    }

}
