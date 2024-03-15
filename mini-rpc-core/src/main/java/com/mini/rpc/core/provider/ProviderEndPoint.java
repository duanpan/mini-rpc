package com.mini.rpc.core.provider;

import com.mini.rpc.core.util.TypeUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            return new RpcResponese(true, result, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new RpcResponese(false, null, e);
        }
    }


    private Object[] paramTypeConver(RpcRequest request, RpcServiceInfo providerInfo) {
        Object[] args = request.getArgs();
        Type[] argsType = providerInfo.getArgsType();
        Object[] resultArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            resultArgs[i] = TypeUtil.cast(args[i], argsType[i]);

        }
        return resultArgs;
    }
}
