package com.mini.rpc.test.consumer;

import com.mini.rpc.test.consumer.biz.SystemService;
import com.mini.rpc.test.provider.api.User;
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

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ConsumerApplication.class, args);
        SystemService systemService = run.getBean(SystemService.class);
        User user = systemService.getUser();
        System.out.println(user);

        User user1 = systemService.listUser(1);
        System.out.println(user1);

    }
}
