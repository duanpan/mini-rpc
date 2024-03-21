package com.mini.rpc.core.loadbalance;

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
    String doChoose(List<String> urls) {
        return urls.get(Math.abs(atomicInteger.getAndIncrement() % urls.size()));
    }

}
