package com.mini.rpc.core;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/3/8
 */
@Data
public class ProxyPlugin implements InvocationHandler {

    private Map<Method, String> serviceSignMap;

    public ProxyPlugin(Map<Method, String> serviceSignMap) {
        this.serviceSignMap = serviceSignMap;
    }

    public static Object wrap(Class<?> interfaces) {
        Method[] methods = interfaces.getMethods();
        Map<Method, String> serviceSignMap = new HashMap();
        for (Method method : methods) {
            String serviceSign = RpcUtil.generateSericeSign(interfaces, method);
            serviceSignMap.put(method, serviceSign);
        }

        return Proxy.newProxyInstance(
                interfaces.getClassLoader(),
                new Class[]{interfaces},
                new ProxyPlugin(serviceSignMap));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serviceName = serviceSignMap.get(method);
        if (serviceName == null) {
            return null;
        }
        System.out.println("----------------------ProxyPlugin---------------------");
        String rsp = callService(serviceName, args);
        RpcResponese rpcResponese = JSONObject.parseObject(rsp, RpcResponese.class);
        String data = JSONObject.toJSONString(rpcResponese.getData());
        return JSONObject.parseObject(data, (Type) method.getReturnType());
    }

    private String callService(String serviceName, Object[] args) {
        Map req = new HashMap();
        req.put("serviceName", serviceName);
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
