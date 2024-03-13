package com.mini.rpc.core.provider;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @Author dp
 * @Date 2024/3/13
 */
@RestController
public class ProviderEndPoint {

    @PostMapping("/invok")
    public RpcResponese rpcInvoker(@RequestBody RpcRequest request) {
        if (!ProviderCache.providers.containsKey(request.getServiceSign())) {
            throw new RuntimeException(request.getServiceSign() + "未找到");
        }

        return invok(request);

    }

    public RpcResponese invok(RpcRequest request) {
        try {
            RpcServiceInfo providerInfo = ProviderCache.providers.get(request.getServiceSign());
            Object[] args = request.getArgs() == null ? request.getArgs() : paramTypeConver(request, providerInfo);
            Method method = providerInfo.getMethod();
            Object result = method.invoke(providerInfo.getService(), args);
            return new RpcResponese(true, result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new RpcResponese(false, null);
    }


    private Object[] paramTypeConver(RpcRequest request, RpcServiceInfo providerInfo) {
        Object[] args = request.getArgs();
        Type[] argsType = providerInfo.getArgsType();
        Object[] resultArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            resultArgs[i] = getValueByType(argsType[i], args[i]);

        }
        return resultArgs;
    }


    Object getValueByType(Type type, Object value) {
        if (value == null || value.equals("")) {
            return value;
        }

        Object newValue;
        String oldValue = value.toString();
        if (type == Integer.TYPE || type == Integer.class) {
            newValue = Integer.parseInt(oldValue);
        } else if (type == Long.TYPE || type == Long.class) {
            newValue = Long.parseLong(oldValue);
        } else if (type == Float.TYPE || type == Float.class) {
            newValue = Float.parseFloat(oldValue);
        } else if (type == Double.TYPE || type == Double.class) {
            newValue = Double.parseDouble(oldValue);
        } else if (type == Boolean.TYPE || type == Boolean.class) {
            newValue = Boolean.parseBoolean(oldValue);
        } else if (type == Character.TYPE || type == Character.class) {
            newValue = oldValue.charAt(0);
        } else if (type == String.class) {
            newValue = oldValue;
        } else {
            String s = JSON.toJSONString(value);
            newValue = JSON.parseObject(s, type);
        }
        return newValue;
    }

}
