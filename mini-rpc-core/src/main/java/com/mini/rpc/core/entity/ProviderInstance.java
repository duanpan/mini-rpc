package com.mini.rpc.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 服务提供者实例
 *
 * @Author dp
 * @Date 2024/4/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderInstance {

    private String protocol;

    private String ip;

    private String port;

    /**
     * 扩展参数
     */
    private Map<String, String> expand;


    public String getCallUrl() {
        String proto = protocol.equals("http") ? "http://" : "";
        return proto.concat(ip).concat(":").concat(port).concat("/").concat("invok");
    }
}
