package com.mini.rpc.core.util;

import com.mini.rpc.core.consumer.ConsumerInvocation;
import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.properties.RpcAppProperties;
import com.mini.rpc.core.provider.RpcServiceInfo;
import com.mini.rpc.core.provider.ProviderInstance;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dp
 * @Date 2024/3/8
 */
@Component
public class RpcBuildHelper {


    public static String buildServiceSign(RpcServiceInfo serviceInfo) {
        return buildServiceSign(serviceInfo.getServiceName(), serviceInfo.getMethodName(), serviceInfo.getArgsType());
    }


    public static String buildServiceSign(String serviceName, String methodName, Type[] argsType) {
        List<String> paramTypes = argsType.length < 1 ? Arrays.asList("none") : Arrays.stream(argsType).map(param -> param.getTypeName()).collect(Collectors.toList());
        Collections.sort(paramTypes);
        String paramSign = paramTypes.stream().collect(Collectors.joining("@"));
        String serviceSign = serviceName.concat(".").concat(methodName).concat("(").concat(paramSign).concat(")");
        return serviceSign;
    }

    @SneakyThrows
    public static String buildInstanceNodeName(ProviderInstance instance) {
        String instanceSign = String.format("/%s/%s/%s/%s/%s:%s", instance.getNamespace(),
                instance.getProtocol(), instance.getEnv(), instance.getServiceSign(), instance.getIp(), instance.getPort());
        return instanceSign;
    }


    public static ProviderInstance buildInstance(RpcAppProperties appProperties, String serviceSign, String ip, String serverPort) {
        ProviderInstance instance = new ProviderInstance();
        instance.setNamespace(appProperties.getNamespace());
        instance.setProtocol(appProperties.getProtocol());
        instance.setEnv(appProperties.getEnv());
        instance.setServiceSign(serviceSign);
        instance.setIp(ip);
        instance.setPort(serverPort);
        return instance;
    }


    public static Object buildJdkProxy(Class<?> itf, RpcContext rpcContext) {
        return Proxy.newProxyInstance(
                itf.getClassLoader(),
                new Class[]{itf},
                new ConsumerInvocation(rpcContext));
    }

}
