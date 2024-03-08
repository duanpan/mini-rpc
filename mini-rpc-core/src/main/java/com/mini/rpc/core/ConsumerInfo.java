package com.mini.rpc.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerInfo {

    /**
     * spring bean 对象
     **/
    private Object bean;
    /**
     * 属性名集合
     **/
    private List<String> filedNames;
}
