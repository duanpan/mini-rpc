package com.mini.rpc.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.mini.rpc.core.provider.RpcRequest;
import com.mini.rpc.core.provider.RpcResponese;
import com.mini.rpc.core.util.RpcUtil;
import com.mini.rpc.core.util.TypeUtil;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author dp
 * @Date 2024/3/12
 */
public class ConsumerInvocation implements InvocationHandler {

    private String serviceName;

    public ConsumerInvocation(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceSign = RpcUtil.buildServiceSign(serviceName, method.getName(), method.getParameterTypes());
        String rsp = callService(serviceSign, args);
        RpcResponese rpcResponese = JSONObject.parseObject(rsp, RpcResponese.class);
        Object data= TypeUtil.cast(rpcResponese.getData(),method.getReturnType());
        return data;
    }


    private String callService(String serviceSign, Object[] args) {
        RpcRequest request=new RpcRequest();
        request.setServiceSign(serviceSign);
        request.setArgs(args);
        String body = JSONObject.toJSONString(request);
        RequestBody requestBody = RequestBody.create(body, MediaType.parse("application/json"));

        OkHttpClient client = new OkHttpClient();
        Request httpRequest = new Request.Builder()
                .url("http://localhost:8080/invok")
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
