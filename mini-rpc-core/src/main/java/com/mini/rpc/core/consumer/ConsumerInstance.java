package com.mini.rpc.core.consumer;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author dp
 * @Date 2024/4/2
 */
@Data
@EqualsAndHashCode
public class ConsumerInstance {
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
}
