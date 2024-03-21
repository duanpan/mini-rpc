package com.mini.rpc.core.loadbalance;

import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/20
 */
public abstract  class AbstractLoadBalancer  implements LoadBalancer{

    @Override
    public String choose(List<String> urls) {
        if (urls == null || urls.size() < 1) {
            return null;
        }
        if (urls.size() == 1) {
            return urls.get(0);
        }

        return doChoose(urls);
    }

    abstract String doChoose(List<String> urls);
}
