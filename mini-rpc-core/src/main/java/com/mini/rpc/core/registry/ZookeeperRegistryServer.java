package com.mini.rpc.core.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * @Author dp
 * @Date 2024/3/21
 */
@Data
public class ZookeeperRegistryServer implements RegistryServer {

    @Autowired
    private Environment environment;
    @Autowired
    private CuratorFramework curatorFramework;


    @Override
    public void register(String serviceName) {
        try {
            String ipPort=InetAddress.getLocalHost().getHostAddress().concat(":").concat(environment.getProperty("server.port"));
            String registServiceName="/mini-rpc".concat("/").concat(serviceName).concat("/").concat(ipPort);
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(registServiceName,"".getBytes());
            System.out.println(registServiceName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
