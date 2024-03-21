package com.mini.rpc.core.util;

import com.mini.rpc.core.provider.RpcServiceInfo;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dp
 * @Date 2024/3/8
 */
public class RpcUtil {


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


}
