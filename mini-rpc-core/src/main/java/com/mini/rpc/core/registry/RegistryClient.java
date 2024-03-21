package com.mini.rpc.core.registry;

import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/21
 */
public interface RegistryClient {

    /**
     * 获取服务
     *
     * @param serviceName
     * @return List<String>
     */
    List<String> getServer(String serviceName);


    /**
     * 添加服务
     */
    public void addServer();


    /**
     * 删除删除
     */
    public void removeServer();
}
