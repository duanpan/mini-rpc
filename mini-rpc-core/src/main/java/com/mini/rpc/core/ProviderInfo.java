package com.mini.rpc.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderInfo {
    /**
     * 服务签名
     **/
    private String serviceName;
    /**
     * 服务对象
     **/
    private Object service;
    /**
     * 方法对象
     **/
    private  Method method;

}
