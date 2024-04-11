package com.mini.rpc.core.loadbalance;

import com.mini.rpc.core.entity.ProviderInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询
 *
 * @Author dp
 * @Date 2024/3/20
 */
public class RoudRobinLoadBalancer extends AbstractLoadBalancer {

    AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    ProviderInstance doChoose(List<ProviderInstance> urls) {
        return urls.get(Math.abs(atomicInteger.getAndIncrement() % urls.size()));
    }

}
