package com.mini.rpc.core.provider;

import com.mini.rpc.core.registry.RegistryCenter;
import com.mini.rpc.core.registry.ZookeeperRegistry;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public ApplicationRunner providerRunner() {
        return new ProviderRunner();
    }

    @Bean
    @ConditionalOnMissingBean(RegistryCenter.class)
    public RegistryCenter registryCenter() {
        return new ZookeeperRegistry();
    }

}
