package com.mini.rpc.core.provider;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 注册实例
 *
 * @Author dp
 * @Date 2024/4/2
 */
@Data
@EqualsAndHashCode
public class ProviderInstance {

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

}
