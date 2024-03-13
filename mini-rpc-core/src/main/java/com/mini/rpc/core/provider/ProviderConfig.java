package com.mini.rpc.core.provider;

import org.springframework.boot.ApplicationRunner;
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

}
