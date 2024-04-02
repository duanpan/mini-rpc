package com.mini.rpc.core.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Author dp
 * @Date 2024/4/2
 */
@Data
@Component
@ConfigurationProperties(prefix = "minirpc.app")
public class RpcAppProperties {
    /**
     * 命名空间
     */
    private String namespace;
    /**
     * 远程调用协议
     */
    private String protocol;
    /**
     * 环境
     */
    private String env;
}
