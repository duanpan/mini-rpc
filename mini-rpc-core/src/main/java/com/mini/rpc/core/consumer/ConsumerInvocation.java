package com.mini.rpc.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.mini.rpc.core.provider.RpcResponese;
import com.mini.rpc.core.util.RpcUtil;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

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
        String data = JSONObject.toJSONString(rpcResponese.getData());
        return JSONObject.parseObject(data, (Type) method.getReturnType());
    }


    private String callService(String serviceSign, Object[] args) {
        Map req = new HashMap();
        req.put("serviceSign", serviceSign);
        req.put("args", args);
        RequestBody requestBody = RequestBody.create(JSONObject.toJSONString(req), MediaType.parse("application/json"));

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/rpc")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            return responseBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
