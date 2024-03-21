package com.mini.rpc.core.provider;

import com.mini.rpc.core.registry.RegistryServer;
import com.mini.rpc.core.registry.ZookeeperRegistryServer;
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
    @ConditionalOnMissingBean(CuratorFramework.class)
    public CuratorFramework curatorFramework() {
        String ZK_URL = "139.159.246.62:2181";
        int TIME_OUT = 6000;
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 5, 6000);
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().retryPolicy(policy).connectString(ZK_URL).sessionTimeoutMs(TIME_OUT).build();
        curatorFramework.start();
        return curatorFramework;
    }

    @Bean
    @ConditionalOnMissingBean(RegistryServer.class)
    public ZookeeperRegistryServer registryServer() {
        return new ZookeeperRegistryServer();
    }


}
