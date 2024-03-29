package com.mini.rpc.core.registry;

import java.util.List;

/**
 * 注册中心
 *
 * @Author dp
 * @Date 2024/3/21
 */
public interface RegistryCenter {

    /**
     * 获取服务
     *
     * @param serviceSign 服务签名
     * @return List<String>
     */
    List<String> fetchServer(String serviceSign);

    /**
     * 注册服务
     */
    default void register(String serviceSign) {
    }

    /**
     * 取消注册服务
     */
    default void unRegister(String serviceSign) {
    }

    /**
     * 启动注册中心
     */
    default void start() {
    }

    /**
     * 停止注册中心
     */
    default void stop() {
    }

    /**
     * 节点变更订阅
     */
    default void subscribe() {
    }


    /**
     * 节点初始化
     */
    default void nodeInit() {
    }

}
