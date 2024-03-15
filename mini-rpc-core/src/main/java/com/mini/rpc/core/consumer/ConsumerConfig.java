package com.mini.rpc.core.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author dp
 * @Date 2024/3/12
 */
@Configuration
public class ConsumerConfig {

    @Bean
    public ConsumerBootStrap consumerBootStrap(){
        return  new ConsumerBootStrap();
    }

    @Bean
    public ApplicationRunner consumerRunner(){
         return  new ConsumerRunner();
    }



}
