package com.mini.rpc.core.start;

import com.mini.rpc.core.annotation.RpcConsumer;
import com.mini.rpc.core.consumer.ConsumerCache;
import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.entity.ConsumerFiled;
import com.mini.rpc.core.loadbalance.LoadBalancer;
import com.mini.rpc.core.properties.RpcAppProperties;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.util.RpcBuildHelper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/7
 */
public class ConsumerBootStrap {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private LoadBalancer loadBalancer;
    @Autowired
    private RegistryCenter registryCenter;
    @Autowired
    private RpcAppProperties appProperties;


    public void start() {
        proxyInject(consumerScan());
        registryCenter.nodeInit();
        registryCenter.subscribe();
    }

    public void stop() {

    }

    /**
     * 扫描带有RpcConsumer注解的属性
     *
     * @return List<Field>
     */
    private List<ConsumerFiled> consumerScan() {
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        List<ConsumerFiled> consumerFileds = new ArrayList();
        for (String beanName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanName);
            Class<?> aClass = bean.getClass();
            while (aClass != null) {
                for (Field field : aClass.getDeclaredFields()) {
                    if (field.isAnnotationPresent(RpcConsumer.class)) {
                        consumerFileds.add(new ConsumerFiled(field, bean));
                    }
                }
                aClass = aClass.getSuperclass();
            }
        }
        return consumerFileds;
    }

    /**
     * 代理注入
     *
     * @param consumerFiledList
     */
    @SneakyThrows
    private void proxyInject(List<ConsumerFiled> consumerFiledList) {
        for (ConsumerFiled consumerFiled : consumerFiledList) {
            //获取服务信息
            RpcContext rpcContext = new RpcContext();
            String serviceName = consumerFiled.getField().getType().getCanonicalName();
            rpcContext.setRegistryCenter(registryCenter);
            rpcContext.setLoadBalancer(loadBalancer);
            rpcContext.setServiceName(serviceName);

            //构建代理对象
            Object proxy = null;
            if (ConsumerCache.proxyCache.containsKey(serviceName)) {
                proxy = ConsumerCache.proxyCache.get(serviceName);
            } else {
                proxy = RpcBuildHelper.buildJdkProxy(consumerFiled.getField().getType(), rpcContext);
                ConsumerCache.proxyCache.put(serviceName, proxy);
            }
            consumerFiled.getField().setAccessible(true);
            consumerFiled.getField().set(consumerFiled.getBean(), proxy);
        }
    }


}
