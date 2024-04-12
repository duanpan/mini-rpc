package com.mini.rpc.core.registry;

import com.mini.rpc.core.entity.ProviderInstance;
import com.mini.rpc.core.entity.ServiceMeta;

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
    List<ProviderInstance> fetchServer(ServiceMeta serviceMeta) throws Exception;

    /**
     * 注册服务
     */
    default void register(ServiceMeta serviceMeta) throws Exception{
    }

    /**
     * 取消注册服务
     */
    default void unRegister(ServiceMeta serviceMeta) throws Exception {
    }

    /**
     * 启动注册中心
     */
    default void start() throws Exception{
    }

    /**
     * 停止注册中心
     */
    default void stop() throws Exception{
    }


    /**
     * 节点变更订阅
     */
    default void subscribe() throws Exception{
    }

    /**
     * 节点初始化
     */
    default void nodeInit() throws Exception{


    }

}
