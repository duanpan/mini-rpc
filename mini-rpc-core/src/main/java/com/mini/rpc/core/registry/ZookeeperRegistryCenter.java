package com.mini.rpc.core.registry;

import com.mini.rpc.core.constans.RpcConstans;
import com.mini.rpc.core.entity.ProviderInstance;
import com.mini.rpc.core.entity.ServiceMeta;
import com.mini.rpc.core.exception.RpcException;
import com.mini.rpc.core.properties.RpcRegistryProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * zookeeper注册中心
 *
 * @Author dp
 * @Date 2024/3/23
 */
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter {

    @Autowired
    private RpcRegistryProperties registryProperties;
    private CuratorFramework curator;

    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(serviceMeta.toNodeUrl(), "".getBytes());
        log.info("服务注册:{}", serviceMeta.toNodeUrl());
    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {
        String nodeUrl = serviceMeta.toNodeUrl();
        if (curator.checkExists().forPath(nodeUrl) == null) {
            return;
        }
        curator.delete().deletingChildrenIfNeeded().forPath(nodeUrl);
        log.info("服务下线:{}", nodeUrl);
    }

    @Override
    public void start() throws Exception {
        if (registryProperties.getType().equals("zookeeper")) {
            String zkHost = registryProperties.getUrl();
            if (StringUtils.isBlank(zkHost)) {
                throw new RuntimeException("连接zookeeper失败,请检查参数:" + RpcConstans.REGISTRY_HOST_ENV);
            }
            RetryPolicy policy = new ExponentialBackoffRetry(1000, 5, 6000);
            curator = CuratorFrameworkFactory.builder().retryPolicy(policy).connectString(zkHost).sessionTimeoutMs(6000).build();
            curator.start();
        }

    }

    @Override
    public void stop() throws Exception {
        curator.close();
    }


    @Override
    public void nodeInit() throws Exception {

    }

    @Override
    public void subscribe() throws Exception {
        try {
            TreeCache cache = TreeCache.newBuilder(curator, RpcConstans.REGISTRY_NAMESPACE).setCacheData(true).setMaxDepth(2).build();
            cache.getListenable().addListener((client, event) -> {
            });
            cache.start();
        } catch (Exception e) {
            throw new RpcException(e);
        }
    }

    @Override
    public List<ProviderInstance> fetchServer(ServiceMeta serviceMeta) throws Exception {
        String serviceNodeName = serviceMeta.getNodeName();
        List<String> nodes = curator.getChildren().forPath(serviceNodeName);

        List<ProviderInstance> providerInstances = nodes.stream().map(node -> {
            ProviderInstance instance = new ProviderInstance();
            instance.setIp(node.substring(0, node.indexOf(":")));
            instance.setPort(node.substring(node.indexOf(":") + 1, node.length()));
            instance.setProtocol(serviceMeta.getProtocol());
            return instance;
        }).collect(Collectors.toList());

        return providerInstances;
    }

}
