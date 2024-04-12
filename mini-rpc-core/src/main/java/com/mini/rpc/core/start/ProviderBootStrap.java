package com.mini.rpc.core.start;

import com.mini.rpc.core.annotation.RpcProvider;
import com.mini.rpc.core.entity.ProviderMeta;
import com.mini.rpc.core.entity.ServiceMeta;
import com.mini.rpc.core.helper.MetaBuildHelper;
import com.mini.rpc.core.provider.ProviderCache;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.util.RpcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.util.Iterator;
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


    public void start() throws Exception {
        providerScan();
        providerRegister();

    }

    public void stop() throws Exception {
        Map<String, ProviderMeta> providers = ProviderCache.providers;
        Iterator<String> iterator = providers.keySet().iterator();
        while (iterator.hasNext()) {
            ServiceMeta serviceMeta = rpcBuildHelper.buildServiceMeta(iterator.next());
            registryCenter.unRegister(serviceMeta);
        }

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

    private void providerRegister() throws Exception {
        Map<String, ProviderMeta> providers = ProviderCache.providers;
        Iterator<String> iterator = providers.keySet().iterator();
        while (iterator.hasNext()) {
            ServiceMeta serviceMeta = rpcBuildHelper.buildServiceMeta(iterator.next());
            registryCenter.register(serviceMeta);
        }
    }

}
