package com.mini.rpc.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 服务消费者定义 元数据
 *
 * @Author dp
 * @Date 2024/4/2
 */
@Data
@EqualsAndHashCode
public class ServiceMeta {

    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 环境名
     */
    private String env;
    /**
     * 服务签名
     */
    private String serviceSign;
    /**
     * 服务IP
     */
    private String ip;
    /**
     * 服务端口
     */
    private String port;

    /**
     * 扩展参数
     */
    private Map<String,String> expand;


    public String toNodeUrl() {
        return String.format("/%s/%s/%s/%s/%s:%s", namespace, protocol, env, serviceSign, ip, port);
    }

    public String getNodeName() {
        return String.format("/%s/%s/%s/%s", namespace, protocol, env, serviceSign);
    }


}
