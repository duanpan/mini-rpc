package com.mini.rpc.core.registry;

import com.mini.rpc.core.constans.RpcConstans;
import com.mini.rpc.core.consumer.ConsumerCache;
import com.mini.rpc.core.provider.ProviderCache;
import com.mini.rpc.core.provider.RpcServiceInfo;
import com.mini.rpc.core.util.MapUtil;
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
import org.springframework.core.env.Environment;

import java.net.InetAddress;
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

    @Autowired
    private Environment environment;
    private CuratorFramework curator;

    @Override
    @SneakyThrows
    public List<String> fetchServer(String serviceSign) {
        return ConsumerCache.providesOnline.get(serviceSign);
    }

    @Override
    @SneakyThrows
    public void register(String serviceSign) {
        if (StringUtils.isBlank(serviceSign)) return;
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty(RpcConstans.SERVER_PORT_ENV);
        String nodeName = RpcConstans.REGISTRY_NAMESPACE.concat("/").concat(serviceSign).concat("/").concat(ip).concat(":").concat(port);
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(nodeName, "".getBytes());
        log.info("服务注册:{}", nodeName);
    }

    @Override
    @SneakyThrows
    public void unRegister(String serviceSign) {
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty(RpcConstans.SERVER_PORT_ENV);
        String nodeName = RpcConstans.REGISTRY_NAMESPACE.concat("/").concat(serviceSign).concat("/").concat(ip).concat(":").concat(port);
        if (curator.checkExists().forPath(nodeName) == null) {
            return;
        }
        curator.delete().deletingChildrenIfNeeded().forPath(nodeName);
        log.info("服务下线:{}", nodeName);
    }

    @Override
    public void start() {
        String zkHost = environment.getProperty(RpcConstans.ZK_REGISTRY_HOST_ENV);
        if (StringUtils.isBlank(zkHost)) {
            throw new RuntimeException("连接zookeeper失败,请检查参数:" + RpcConstans.ZK_REGISTRY_HOST_ENV);
        }
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 5, 6000);
        curator = CuratorFrameworkFactory.builder().retryPolicy(policy).connectString(zkHost).sessionTimeoutMs(6000).build();
        curator.start();
    }

    @Override
    public void stop() {
        Map<String, RpcServiceInfo> providers = ProviderCache.providers;
        if (providers.size() > 0) return;
        providers.forEach((k, v) -> unRegister(k));
    }


    @Override
    @SneakyThrows
    public  void nodeInit() {
        doNodeInit(curator);
    }

    @Override
    @SneakyThrows
    public void subscribe() {
        TreeCache cache=TreeCache.newBuilder(curator, RpcConstans.REGISTRY_NAMESPACE).setCacheData(true).setMaxDepth(2).build();
        cache.getListenable().addListener((client, event) -> {
            doNodeInit(client);
        });
        cache.start();
    }


    @SneakyThrows
    public synchronized void doNodeInit(CuratorFramework client) {
        ConsumerCache.providesOnline.clear();
        List<String> services = client.getChildren().forPath(RpcConstans.REGISTRY_NAMESPACE);
        for (String service : services) {
            String servicePath = RpcConstans.REGISTRY_NAMESPACE.concat("/").concat(service);
            List<String> nodes = client.getChildren().forPath(servicePath);
            MapUtil.addMultiValue(ConsumerCache.providesOnline, service, nodes);
        }
    }

}
