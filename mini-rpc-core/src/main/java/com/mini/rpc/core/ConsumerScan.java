package com.mini.rpc.core;

import com.mini.rpc.core.annotation.RpcConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@Component
public class ConsumerScan implements ApplicationListener<ApplicationStartedEvent> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, Object> compnentBeans = applicationContext.getBeansWithAnnotation(Component.class);
        //获得带有 RpcConsumer 属性的对象
        List<ConsumerInfo> consumerInfos = getBeanByRpcConsumer(compnentBeans);

        for (ConsumerInfo consumerInfo : consumerInfos) {
            List<String> filedNames = consumerInfo.getFiledNames();
            for (String filedName : filedNames) {
                try {
                    Field field = consumerInfo.getBean().getClass().getDeclaredField(filedName);
                    Class<?> type = field.getType();
                    if (type.isInterface()) {
                        Object proxy = ProxyPlugin.wrap(type);
                        try {
                            field.setAccessible(true);
                            field.set(consumerInfo.getBean(),proxy);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private List<ConsumerInfo> getBeanByRpcConsumer(Map<String, Object> compnentBeans) {
        List<ConsumerInfo> consumerInfos = new ArrayList();
        compnentBeans.forEach((k, v) -> {
            Field[] fields = v.getClass().getDeclaredFields();
            List<String> fds = new ArrayList();
            for (Field field : fields) {
                if (field.isAnnotationPresent(RpcConsumer.class)) {
                    fds.add(field.getName());
                }
            }
            if (fds.size() > 0) {
                ConsumerInfo consumerInfo = new ConsumerInfo(v, fds);
                consumerInfos.add(consumerInfo);
            }
        });
        return consumerInfos;

    }
}
