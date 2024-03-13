package com.mini.rpc.core.consumer;

import com.mini.rpc.core.util.ProxyPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/7
 */
public class ConsumerBootStrap{

    @Autowired
    private ApplicationContext applicationContext;

    public void consumerStart() {
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
                Object proxy = buildProxy(annotationFiled);
                try {
                    annotationFiled.setAccessible(true);
                    annotationFiled.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private Object buildProxy(Field annotationFiled) {
        boolean anInterface = annotationFiled.getType().isInterface();
        Object proxy = anInterface ? ProxyPlugin.buildJdkProxy(annotationFiled.getType()) : ProxyPlugin.buildCglibProxy(annotationFiled.getType());
        return proxy;
    }

}
