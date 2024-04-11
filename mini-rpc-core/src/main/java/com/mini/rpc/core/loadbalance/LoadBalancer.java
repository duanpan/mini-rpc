package com.mini.rpc.core.loadbalance;

import com.mini.rpc.core.entity.ProviderInstance;

import java.util.List;

/**
 * 负载均衡
 *
 * @Author dp
 * @Date 2024/3/20
 */
public interface LoadBalancer {

    ProviderInstance choose(List<ProviderInstance> urls);

}
