package com.mini.rpc.core.properties;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author dp
 * @Date 2024/4/2
 */
@Data
@Component
@ConfigurationProperties(prefix = "minirpc.registry")
public class RpcRegistryProperties {
    /**
     * 注册中心类型
     */
    private String type;
    /**
     * 注册中心地址
     */
    private String url;

}
