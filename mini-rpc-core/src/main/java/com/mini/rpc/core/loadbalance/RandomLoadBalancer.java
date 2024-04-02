package com.mini.rpc.core.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载
 *
 * @Author dp
 * @Date 2024/3/20
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {

    Random random = new Random();

    @Override
    String doChoose(List<String> urls) {
        int i = random.nextInt(urls.size());
        System.out.println(urls);
        System.out.println(i);
        System.out.println(urls.get(i));
        return urls.get(i);
    }
}
