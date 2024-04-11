package com.mini.rpc.core.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.entity.ProviderInstance;
import com.mini.rpc.core.entity.ServiceMeta;
import com.mini.rpc.core.provider.RpcRequest;
import com.mini.rpc.core.provider.RpcResponese;
import com.mini.rpc.core.util.TypeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/12
 */
public class ConsumerInvocation implements InvocationHandler {

    private RpcContext context;


    public ConsumerInvocation(RpcContext context) {
        this.context = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ServiceMeta serviceMeta = context.getRpcBuildHelper().buildServiceMeta(context.getServiceName(), method.getName(), method.getParameterTypes());
        List<ProviderInstance> providers = context.getRegistryCenter().fetchServer(serviceMeta);
        ProviderInstance provider = context.getLoadBalancer().choose(providers);

        RpcRequest request = new RpcRequest(serviceMeta.getServiceSign(), args);
        Object rsp = context.getHttpClient().post(provider.getCallUrl(), JSONObject.toJSONString(request));
        RpcResponese rpcResponese = JSON.parseObject(rsp.toString(), RpcResponese.class);
        Object data = TypeUtil.cast(rpcResponese.getData(), method.getReturnType(), method.getGenericReturnType());
        return data;
    }


}
