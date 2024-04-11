package com.mini.rpc.core.loadbalance;

import com.mini.rpc.core.entity.ProviderInstance;

import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/20
 */
public abstract  class AbstractLoadBalancer  implements LoadBalancer{

    @Override
    public ProviderInstance choose(List<ProviderInstance> urls) {
        if (urls == null || urls.size() < 1) {
            return null;
        }
        if (urls.size() == 1) {
            return urls.get(0);
        }

        return doChoose(urls);
    }

    abstract ProviderInstance doChoose(List<ProviderInstance> urls);
}
