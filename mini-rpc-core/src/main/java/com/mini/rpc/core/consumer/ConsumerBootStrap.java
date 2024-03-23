package com.mini.rpc.core.consumer;

import com.mini.rpc.core.context.RpcContext;
import com.mini.rpc.core.loadbalance.LoadBalancer;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.util.RpcUtil;
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
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

        //扫描带有RpcConsumer注解的属性
        for (String beanName : beanDefinitionNames) {
            List<Field> annotationFileds = new ArrayList<>();
            Object bean = applicationContext.getBean(beanName);
            Class<?> aClass = bean.getClass();
            while (aClass != null) {
                Field[] fields = aClass.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(RpcConsumer.class)) {
                        annotationFileds.add(field);
                    }
                }
                aClass = aClass.getSuperclass();
            }

            //创建代理 注入对象
            for (Field annotationFiled : annotationFileds) {
                RpcContext rpcContext = new RpcContext(registryCenter, loadBalancer, annotationFiled.getType().getCanonicalName());
                Object proxy = RpcUtil.buildJdkProxy(annotationFiled.getType(), rpcContext);
                try {
                    annotationFiled.setAccessible(true);
                    annotationFiled.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

    }


}
