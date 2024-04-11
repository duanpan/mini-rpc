package com.mini.rpc.core.helper;

import com.mini.rpc.core.consumer.ConsumerInvocation;
import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.entity.ServiceMeta;
import com.mini.rpc.core.properties.RpcAppProperties;
import com.mini.rpc.core.entity.ProviderMeta;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dp
 * @Date 2024/3/8
 */
public class MetaBuildHelper {
    @Autowired
    private RpcAppProperties appProperties;
    @Value("${server.port}")
    private String serverPort;

    public  ServiceMeta buildServiceMeta(String serviceName, String methodName, Type[] argsType) {
        List<String> paramTypes = argsType.length < 1 ? Arrays.asList("none") : Arrays.stream(argsType).map(param -> param.getTypeName()).collect(Collectors.toList());
        Collections.sort(paramTypes);
        String paramSign = paramTypes.stream().collect(Collectors.joining("@"));
        String serviceSign = serviceName.concat(".").concat(methodName).concat("(").concat(paramSign).concat(")");
        return buildServiceMeta(serviceSign);
    }

    public ServiceMeta buildServiceMeta(String serviceSign) {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            ServiceMeta instance = new ServiceMeta();
            instance.setNamespace(appProperties.getNamespace());
            instance.setProtocol(appProperties.getProtocol());
            instance.setEnv(appProperties.getEnv());
            instance.setServiceSign(serviceSign);
            instance.setIp(ip);
            instance.setPort(serverPort);
            return instance;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }


}
