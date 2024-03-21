package com.mini.rpc.core.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/21
 */
public class ZookeeperRegistryClient implements RegistryClient {

    @Autowired
    private CuratorFramework curatorFramework;

    @Override
    public List<String> getServer(String serviceSign) {
        try {
            String zkNodeName = "/mini-rpc/".concat(serviceSign);
            if (curatorFramework.checkExists().forPath(zkNodeName) == null) {
                System.out.println(zkNodeName + "未注册任何服务");
                return null;
            }
            List<String> serviceUrls = curatorFramework.getChildren().forPath(zkNodeName);
            return serviceUrls;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addServer() {

    }

    @Override
    public void removeServer() {

    }
}
