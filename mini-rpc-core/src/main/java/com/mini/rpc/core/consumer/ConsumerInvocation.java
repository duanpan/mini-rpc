package com.mini.rpc.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.provider.RpcRequest;
import com.mini.rpc.core.provider.RpcResponese;
import com.mini.rpc.core.util.RpcUtil;
import com.mini.rpc.core.util.TypeUtil;
import okhttp3.*;

import java.io.IOException;
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
        String serviceSign = RpcUtil.buildServiceSign(context.getServiceName(), method.getName(), method.getParameterTypes());
        List<String> providers = context.getRegistryCenter().fetchServer(serviceSign);
        String host = context.getLoadBalancer().choose(providers);
        String remoteUrl = "http://".concat(host).concat("/").concat("invok");
        RpcRequest request = new RpcRequest(serviceSign, args);
        String rsp = callService(request, remoteUrl);
        RpcResponese rpcResponese = JSONObject.parseObject(rsp, RpcResponese.class);

        Object data = TypeUtil.cast(rpcResponese.getData(), method.getReturnType());
        return data;
    }


    private String callService(RpcRequest request, String url) {
        String body = JSONObject.toJSONString(request);
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient();
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(httpRequest).execute();
            String responseBody = response.body().string();
            return responseBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
