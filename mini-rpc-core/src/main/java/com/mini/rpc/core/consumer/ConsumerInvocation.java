package com.mini.rpc.core.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.entity.ProviderInstance;
import com.mini.rpc.core.entity.ServiceMeta;
import com.mini.rpc.core.filter.Filter;
import com.mini.rpc.core.provider.RpcRequest;
import com.mini.rpc.core.provider.RpcResponese;
import com.mini.rpc.core.util.TypeUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
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
        List<Filter> filters = context.getFilters();
        ServiceMeta serviceMeta = context.getRpcBuildHelper().buildServiceMeta(context.getServiceName(), method.getName(), method.getParameterTypes());
        List<ProviderInstance> providers = context.getRegistryCenter().fetchServer(serviceMeta);
        ProviderInstance provider = context.getLoadBalancer().choose(providers);
        RpcRequest request = RpcRequest.builder()
                .serviceSign(serviceMeta.getServiceSign())
                .args(args).build();

        //过滤器 before
        filters.forEach(f -> f.pre(request));

        //执行请求
        RpcResponese responese = invoke(provider.getCallUrl(), request);

        //过滤器 after
        RpcResponese finalResponese = responese;
        filters.forEach(f -> f.post(request, finalResponese));

        Object data = TypeUtil.cast(responese.getData(), method.getReturnType(), method.getGenericReturnType());
        return data;
    }

    private RpcResponese invoke(String callUrl, RpcRequest request) throws Throwable {
        RpcResponese responese = null;
        int retry = 3 + 1;
        while (retry > 0) {
            try {
                retry--;
                responese = doInvoke(callUrl, request);
            } catch (SocketTimeoutException timeoutException) {
                if (retry == 0) {
                    throw new RuntimeException("请求超时");
                }
                System.out.println("【超时重试】-------->");
            }

            if (responese != null) {
                break;
            }

        }
        return responese;
    }


    private RpcResponese doInvoke(String url, RpcRequest request) throws Throwable {
        Object rsp = context.getHttpClient().post(url, JSONObject.toJSONString(request));
        RpcResponese rpcResponese = JSON.parseObject(rsp.toString(), RpcResponese.class);
        return rpcResponese;
    }


}
