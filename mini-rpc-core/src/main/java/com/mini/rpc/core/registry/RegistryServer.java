package com.mini.rpc.core.registry;

/**
 * @Author dp
 * @Date 2024/3/21
 */
public interface RegistryServer {

    /**
     * 注册服务
     *
     * @param serviceName 服务名
     */
    void register(String serviceName);

}
