package com.mini.rpc.core.start;

import com.mini.rpc.core.registry.RegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import javax.annotation.PreDestroy;

/**
 * @Author dp
 * @Date 2024/3/12
 */
public class MiniRpcBootStrap implements ApplicationRunner {

    @Autowired
    private ProviderBootStrap providerBootStrap;
    @Autowired
    private ConsumerBootStrap consumerBootStrap;
    @Autowired
    private RegistryCenter registryCenter;


    @Override
    public void run(ApplicationArguments args) {
        try {
            registryCenter.start();
            providerBootStrap.start();
            consumerBootStrap.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @PreDestroy
    public void stop() {
        try {
            consumerBootStrap.stop();
            providerBootStrap.stop();
            registryCenter.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
