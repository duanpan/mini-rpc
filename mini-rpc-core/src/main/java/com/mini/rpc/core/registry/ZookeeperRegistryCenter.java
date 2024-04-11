package com.mini.rpc.core.registry;

import com.google.common.collect.Lists;
import com.mini.rpc.core.constans.RpcConstans;
import com.mini.rpc.core.consumer.ConsumerCache;
import com.mini.rpc.core.entity.ProviderInstance;
import com.mini.rpc.core.properties.RpcAppProperties;
import com.mini.rpc.core.properties.RpcRegistryProperties;
import com.mini.rpc.core.provider.ProviderCache;
import com.mini.rpc.core.entity.ProviderMeta;
import com.mini.rpc.core.entity.ServiceMeta;
import com.mini.rpc.core.helper.MetaBuildHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * zookeeper注册中心
 *
 * @Author dp
 * @Date 2024/3/23
 */
@Slf4j
public class ZookeeperRegistryCenter implements RegistryCenter {

    private CuratorFramework curator;
    @Autowired
    private RpcAppProperties appProperties;
    @Autowired
    private RpcRegistryProperties registryProperties;
    @Autowired
    private MetaBuildHelper rpcBuildHelper;


    @Override
    @SneakyThrows
    public void register(ServiceMeta serviceMeta) {
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(serviceMeta.toNodeUrl(), "".getBytes());
        log.info("服务注册:{}", serviceMeta.toNodeUrl());
    }

    @Override
    @SneakyThrows
    public void unRegister(ServiceMeta serviceMeta) {
        String nodeUrl = serviceMeta.toNodeUrl();
        if (curator.checkExists().forPath(nodeUrl) == null) {
            return;
        }
        curator.delete().deletingChildrenIfNeeded().forPath(nodeUrl);
        log.info("服务下线:{}", nodeUrl);
    }

    @Override
    public void start() {
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
    @SneakyThrows
    public void stop() {
        Map<String, ProviderMeta> providers = ProviderCache.providers;
        providers.forEach((k, v) -> {
            ServiceMeta serviceMeta = rpcBuildHelper.buildServiceMeta(k);
            unRegister(serviceMeta);
        });
    }


    @Override
    @SneakyThrows
    public void nodeInit() {
        doNodeInit(curator);
    }

    @Override
    @SneakyThrows
    public void subscribe() {
        TreeCache cache = TreeCache.newBuilder(curator, RpcConstans.REGISTRY_NAMESPACE).setCacheData(true).setMaxDepth(2).build();
        cache.getListenable().addListener((client, event) -> {
            doNodeInit(client);
        });
        cache.start();
    }

    @Override
    @SneakyThrows
    public List<ProviderInstance> fetchServer(ServiceMeta serviceMeta) {
        String serviceNodeName = serviceMeta.getNodeName();
        List<String> nodes = curator.getChildren().forPath(serviceNodeName);

        List<ProviderInstance> providerInstances = nodes.stream().map(node -> {
            try {
                ProviderInstance instance = new ProviderInstance();
                instance.setIp(node.substring(0, node.indexOf(":")));
                instance.setPort(node.substring(node.indexOf(":")+1, node.length()));
                instance.setProtocol(serviceMeta.getProtocol());
                return instance;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());

        return providerInstances;
    }

    @SneakyThrows
    public void doNodeInit(CuratorFramework client) {
        String appPrefix = String.format("/%s/%s/%s", appProperties.getNamespace(), appProperties.getProtocol(),
                appProperties.getEnv());
        List<String> services = client.getChildren().forPath(appPrefix);
        for (String service : services) {
            String servicePath = appPrefix.concat("/").concat(service);
            List<String> nodes = client.getChildren().forPath(servicePath);
            if (ConsumerCache.providesOnline.containsKey(service)) {
                ConsumerCache.providesOnline.get(service).addAll(nodes);
            } else {
                ConsumerCache.providesOnline.put(service, Lists.newArrayList(nodes));
            }
        }
    }


}
