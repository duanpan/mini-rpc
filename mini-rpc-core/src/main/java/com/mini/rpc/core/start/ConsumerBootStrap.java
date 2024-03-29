package com.mini.rpc.core.start;

import com.mini.rpc.core.annotation.RpcConsumer;
import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.entity.ConsumerFiled;
import com.mini.rpc.core.loadbalance.LoadBalancer;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.util.RpcUtil;
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


    public void start() {
        proxyInject(consumerScan());
        registryCenter.nodeInit();
        registryCenter.subscribe();
    }

    public void stop(){

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
            RpcContext rpcContext = new RpcContext(registryCenter, loadBalancer, consumerFiled.getField().getType().getCanonicalName());
            Object proxy = RpcUtil.buildJdkProxy(consumerFiled.getField().getType(), rpcContext);
            consumerFiled.getField().setAccessible(true);
            consumerFiled.getField().set(consumerFiled.getBean(), proxy);
        }
    }


}
