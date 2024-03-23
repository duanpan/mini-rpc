package com.mini.rpc.core.registry;

import com.mini.rpc.core.constans.RpcConstans;
import com.mini.rpc.core.provider.ProviderCache;
import com.mini.rpc.core.provider.RpcServiceInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
public class ZookeeperRegistry implements RegistryCenter {

    @Autowired
    private Environment environment;
    private CuratorFramework curator;

    @Override
    @SneakyThrows
    public List<String> fetchServer(String serviceSign) {
        String nodeName = RpcConstans.REGISTRY_NAMESPACE.concat(serviceSign);
        if (curator.checkExists().forPath(nodeName) == null) {
            return null;
        }
        return curator.getChildren().forPath(nodeName);
    }

    @Override
    @SneakyThrows
    public void register(String serviceSign) {
        if (StringUtils.isBlank(serviceSign)) return;
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty(RpcConstans.SERVER_PORT_ENV);
        String nodeName = RpcConstans.REGISTRY_NAMESPACE.concat(serviceSign).concat("/").concat(ip).concat(":").concat(port);
        curator.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(nodeName, "".getBytes());
        log.info("服务注册:{}", nodeName);
    }

    @Override
    @SneakyThrows
    public void unRegister(String serviceSign) {
        String nodeName = RpcConstans.REGISTRY_NAMESPACE.concat(serviceSign);
        if (curator.checkExists().forPath(nodeName) == null) return;
        curator.delete().forPath(nodeName);
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
}
