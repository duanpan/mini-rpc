package com.mini.rpc.test.consumer;

import com.mini.rpc.core.annotation.RpcConsumer;
import com.mini.rpc.test.consumer.biz.SystemService;
import com.mini.rpc.test.consumer.test.TestTypeCastService;
import com.mini.rpc.test.provider.api.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@RestController
@SpringBootApplication(scanBasePackages = "com.mini.rpc")
public class ConsumerApplication {

    @RpcConsumer
    private  UserService userService;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ConsumerApplication.class, args);
        TestTypeCastService typeCastService = run.getBean(TestTypeCastService.class);
        typeCastService.typeCast();
    }
}
