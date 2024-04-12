package com.mini.rpc.core.provider;

import com.mini.rpc.core.entity.ProviderMeta;
import com.mini.rpc.core.enums.ResultCode;
import com.mini.rpc.core.exception.RpcException;
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
public class ProviderInvoker {

    @PostMapping("/invok")
    public RpcResponese rpcInvoker(@RequestBody RpcRequest request) {
        if (!ProviderCache.providers.containsKey(request.getServiceSign())) {
            return new RpcResponese(ResultCode.ERROR_CLIENT, new RpcException(request.getServiceSign() + "未找到"));
        }

        try {
            ProviderMeta providerInfo = ProviderCache.providers.get(request.getServiceSign());
            Object[] args = request.getArgs() == null ? request.getArgs() : paramTypeConver(request, providerInfo);
            Method method = providerInfo.getMethod();
            Object result = method.invoke(providerInfo.getService(), args);
            return new RpcResponese(result);
        } catch (Exception e) {
            return new RpcResponese(ResultCode.ERROR_SERVER, new RpcException(e));
        }
    }


    private Object[] paramTypeConver(RpcRequest request, ProviderMeta providerInfo) {
        Object[] args = request.getArgs();
        Method method = providerInfo.getMethod();
        Class<?>[] argsType = method.getParameterTypes();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Object[] resultArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            resultArgs[i] = TypeUtil.cast(args[i], argsType[i], genericParameterTypes[i]);
        }
        return resultArgs;
    }
}
