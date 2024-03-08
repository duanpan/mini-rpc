package com.mini.rpc.core;

import java.lang.reflect.Method;
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


    public static String generateSericeSign(Class<?> interfaces, Method method) {
        Type[] parameterTypes = method.getGenericParameterTypes();
        List<String> paramTypes = parameterTypes.length < 1 ? Arrays.asList("void") : Arrays.stream(parameterTypes).map(param -> param.getTypeName()).collect(Collectors.toList());
        Collections.sort(paramTypes);
        String serviceName = interfaces.getName();
        String methodName = method.getName();
        String paramSign = paramTypes.stream().collect(Collectors.joining("@"));
        String serviceSign = serviceName.concat(".").concat(methodName).concat("(").concat(paramSign).concat(")");
        return serviceSign;
    }
}
