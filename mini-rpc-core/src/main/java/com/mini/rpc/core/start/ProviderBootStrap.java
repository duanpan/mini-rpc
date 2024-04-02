package com.mini.rpc.core.start;

import com.mini.rpc.core.properties.RpcAppProperties;
import com.mini.rpc.core.provider.ProviderCache;
import com.mini.rpc.core.annotation.RpcProvider;
import com.mini.rpc.core.provider.RpcServiceInfo;
import com.mini.rpc.core.provider.ProviderInstance;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.util.RpcBuildHelper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private Environment environment;
    @Autowired
    private RpcAppProperties appProperties;
    @Value("${server.port}")
    private String serverPort;


    public void start() {
        providerScan();
        providerRegister();

    }

    @SneakyThrows
    public void stop() {
        ProviderCache.providers.forEach((k, v) -> {
            try {
                String ip = InetAddress.getLocalHost().getHostAddress();
                ProviderInstance providerInstance = RpcBuildHelper.buildInstance(appProperties, k, ip, serverPort);
                registryCenter.unRegister(providerInstance);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
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
                    RpcServiceInfo serviceInfo = new RpcServiceInfo();
                    serviceInfo.setServiceName(itf.getCanonicalName());
                    serviceInfo.setService(service);
                    serviceInfo.setMethodName(method.getName());
                    serviceInfo.setMethod(method);
                    serviceInfo.setArgsType(method.getParameterTypes());
                    String serviceSign = RpcBuildHelper.buildServiceSign(serviceInfo);
                    ProviderCache.providers.put(serviceSign, serviceInfo);
                }
            }
        }
    }

    @SneakyThrows
    private void providerRegister() {
        String ip = InetAddress.getLocalHost().getHostAddress();
        ProviderCache.providers.forEach((k, v) -> {
            ProviderInstance providerInstance = RpcBuildHelper.buildInstance(appProperties, k, ip, serverPort);
            registryCenter.register(providerInstance);
        });
    }

}
