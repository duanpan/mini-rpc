package com.mini.rpc.core.provider;

import com.mini.rpc.core.properties.RpcRegistryProperties;
import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.registry.ZookeeperRegistryCenter;
import com.mini.rpc.core.start.ProviderBootStrap;
import com.mini.rpc.core.start.MiniRpcBootStrap;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @Author dp
 * @Date 2024/3/12
 */
@Configuration
public class ProviderConfig {

    @Bean
    public ProviderBootStrap providerBootStrap() {
        return new ProviderBootStrap();
    }

    @Bean
    public ApplicationRunner miniRpcBootStrap() {
        return new MiniRpcBootStrap();
    }

    @Bean
    @ConditionalOnMissingBean(RegistryCenter.class)
    public RegistryCenter registryCenter() {
        return new ZookeeperRegistryCenter();
    }

}
