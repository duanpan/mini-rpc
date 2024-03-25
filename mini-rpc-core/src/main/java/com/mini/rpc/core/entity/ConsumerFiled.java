package com.mini.rpc.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @Author dp
 * @Date 2024/3/25
 */
@Data
@AllArgsConstructor
public class ConsumerFiled {

    private Field  field;
    private Object bean;
}
