package com.mini.rpc.core.loadbalance;

import java.util.List;

/**
 * 负载均衡
 *
 * @Author dp
 * @Date 2024/3/20
 */
public interface LoadBalancer {

    String choose(List<String> urls);

}
