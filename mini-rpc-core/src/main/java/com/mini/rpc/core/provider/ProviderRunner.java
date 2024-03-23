package com.mini.rpc.core.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

/**
 * @Author dp
 * @Date 2024/3/12
 */
public class ProviderRunner implements ApplicationRunner {

    @Autowired
    private ProviderBootStrap providerBootStrap;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        providerBootStrap.start();
    }
}
