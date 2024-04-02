package com.mini.rpc.core.registry;

import com.mini.rpc.core.constans.RpcConstans;
import com.mini.rpc.core.consumer.ConsumerCache;
import com.mini.rpc.core.properties.RpcAppProperties;
import com.mini.rpc.core.properties.RpcRegistryProperties;
import com.mini.rpc.core.provider.ProviderCache;
import com.mini.rpc.core.provider.RpcServiceInfo;
import com.mini.rpc.core.provider.ProviderInstance;
import com.mini.rpc.core.util.MapUtil;
import com.mini.rpc.core.util.RpcBuildHelper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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
    @Value("${server.port}")
    private String serverPort;


    @Override
    @SneakyThrows
    public void register(ProviderInstance instance) {
        String nodeName = RpcBuildHelper.buildInstanceNodeName(instance);
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(nodeName, "".getBytes());
        log.info("服务注册:{}", nodeName);
    }

    @Override
    @SneakyThrows
    public void unRegister(ProviderInstance instance) {
        String nodeName = RpcBuildHelper.buildInstanceNodeName(instance);
        if (curator.checkExists().forPath(nodeName) == null) {
            return;
        }
        curator.delete().deletingChildrenIfNeeded().forPath(nodeName);
        log.info("服务下线:{}", nodeName);
    }

    @Override
    public void start() {
        if (registryProperties.getType().equals("zk")) {
            String zkHost = registryProperties.getUrl();
            if (StringUtils.isBlank(zkHost)) {
                throw new RuntimeException("连接zookeeper失败,请检查参数:" + RpcConstans.ZK_REGISTRY_HOST_ENV);
            }
            RetryPolicy policy = new ExponentialBackoffRetry(1000, 5, 6000);
            curator = CuratorFrameworkFactory.builder().retryPolicy(policy).connectString(zkHost).sessionTimeoutMs(6000).build();
            curator.start();
        }

    }

    @Override
    @SneakyThrows
    public void stop() {
        Map<String, RpcServiceInfo> providers = ProviderCache.providers;
        String ip = InetAddress.getLocalHost().getHostAddress();
        providers.forEach((k, v) -> {
            ProviderInstance providerInstance = RpcBuildHelper.buildInstance(appProperties, k, ip, serverPort);
            unRegister(providerInstance);
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
    public List<String> fetchServer(String serviceSign) {
        return new ArrayList<>(ConsumerCache.providesOnline.get(serviceSign));
    }

    @SneakyThrows
    public synchronized void doNodeInit(CuratorFramework client) {
        String appPrefix = String.format("/%s/%s/%s", appProperties.getNamespace(), appProperties.getProtocol(),
                appProperties.getEnv());
        List<String> services = client.getChildren().forPath(appPrefix);
        for (String service : services) {
            String servicePath = appPrefix.concat("/").concat(service);
            List<String> nodes = client.getChildren().forPath(servicePath);
            if (ConsumerCache.providesOnline.containsKey(service)) {
                ConsumerCache.providesOnline.get(service).addAll(nodes);
            } else {
                ConsumerCache.providesOnline.put(service, new HashSet<>(nodes));
            }
        }
    }


}
