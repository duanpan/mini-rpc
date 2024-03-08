package com.mini.rpc.test.provider;

import com.mini.rpc.core.ProviderInfo;
import com.mini.rpc.core.ProviderScan;
import com.mini.rpc.core.RpcRequest;
import com.mini.rpc.core.RpcResponese;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@RestController
@SpringBootApplication(scanBasePackages = "com.mini.rpc")
public class ProviderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ProviderApplication.class, args);
    }


    @PostMapping("rpc")
    public RpcResponese rpcInvoker(@RequestBody RpcRequest request) {
        return invok(request);

    }


    public RpcResponese invok(RpcRequest request) {
       if (!ProviderScan.providers.containsKey(request.getServiceName())) {
            throw new RuntimeException(request.getServiceName() + "未找到");
        }

        try {
            ProviderInfo providerInfo = ProviderScan.providers.get(request.getServiceName());
            Method method = providerInfo.getMethod();
            Object result = method.invoke(providerInfo.getService(), request.getArgs());
            return new RpcResponese(true, result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return new RpcResponese(false, null);
    }

}
