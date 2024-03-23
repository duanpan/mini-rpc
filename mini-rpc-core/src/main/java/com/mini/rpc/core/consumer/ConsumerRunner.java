package com.mini.rpc.core.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @Author dp
 * @Date 2024/3/12
 */

public class ConsumerRunner implements ApplicationRunner {

    @Autowired
    private ConsumerBootStrap consumerBootStrap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        consumerBootStrap.start();
    }
}
