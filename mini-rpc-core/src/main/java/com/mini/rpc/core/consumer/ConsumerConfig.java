package com.mini.rpc.core.consumer;

import com.mini.rpc.core.loadbalance.LoadBalancer;
import com.mini.rpc.core.loadbalance.RandomLoadBalancer;
import com.mini.rpc.core.loadbalance.RoudRobinLoadBalancer;
import com.mini.rpc.core.route.ConfRouter;
import com.mini.rpc.core.route.Router;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
    public ApplicationRunner consumerRunner() {
        return new ConsumerRunner();
    }

    @Bean
    @ConditionalOnMissingBean(LoadBalancer.class)
    public LoadBalancer loadBalancer() {
        return new RandomLoadBalancer();
    }

    @Bean
    @ConditionalOnMissingBean(Router.class)
    public Router router(@Autowired Environment environment) {
        return new ConfRouter(environment);
    }

}
