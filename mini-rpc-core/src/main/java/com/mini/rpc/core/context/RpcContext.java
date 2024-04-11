package com.mini.rpc.core.context;

import com.mini.rpc.core.consumer.HttpClient;
import com.mini.rpc.core.filter.Filter;
import com.mini.rpc.core.loadbalance.LoadBalancer;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.helper.MetaBuildHelper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RpcContext {
    private MetaBuildHelper rpcBuildHelper;
    private RegistryCenter registryCenter;
    private LoadBalancer loadBalancer;
    private String serviceName;
    private HttpClient httpClient;
    private List<Filter> filters;

}
