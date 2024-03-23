package com.mini.rpc.core.context;

import com.mini.rpc.core.loadbalance.LoadBalancer;
import com.mini.rpc.core.registry.RegistryCenter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dp
 * @Date 2024/3/21
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RpcContext {

    private RegistryCenter registryCenter;
    private LoadBalancer loadBalancer;
    private String serviceName;

}
