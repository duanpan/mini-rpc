package com.mini.rpc.core.consumer;

import com.mini.rpc.core.filter.ParamTransferFilter;
import com.mini.rpc.core.filter.Filter;
import com.mini.rpc.core.loadbalance.LoadBalancer;
import com.mini.rpc.core.loadbalance.RandomLoadBalancer;
import com.mini.rpc.core.start.ConsumerBootStrap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author dp
 * @Date 2024/3/12
 */
@Configuration
public class ConsumerConfig {

    @Bean
    public ConsumerBootStrap consumerBootStrap() {
        return new ConsumerBootStrap();
    }

    @Bean
    @ConditionalOnMissingBean(LoadBalancer.class)
    public LoadBalancer loadBalancer() {
        return new RandomLoadBalancer();
    }

    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient() {
        return new MyOkHttpClient();
    }

    @Bean
    public Filter paramFilter() {
        return new ParamTransferFilter();
    }

}
