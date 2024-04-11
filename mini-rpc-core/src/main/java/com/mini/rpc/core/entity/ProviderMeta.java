package com.mini.rpc.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderMeta {
    /**
     * 服务名
     **/
    private String serviceName;
    /**
     * 方法名
     **/
    private String methodName;
    /**
     * 参数类型
     **/
    private Type[] argsType;
    /**
     * 服务对象
     **/
    private Object service;

    /**
     * 方法对象
     **/
    private  Method method;

}
