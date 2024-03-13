package com.mini.rpc.test.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@SpringBootApplication(scanBasePackages = "com.mini.rpc")
public class ProviderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ProviderApplication.class, args);
    }

}
