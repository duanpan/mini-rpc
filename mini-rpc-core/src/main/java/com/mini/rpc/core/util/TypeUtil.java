package com.mini.rpc.core.util;
import com.alibaba.fastjson.JSON;
import java.lang.reflect.*;

/**
 * @Author dp
 * @Date 2024/3/15
 */
public class TypeUtil {


    public static Object cast(Object value, Type type, Type genericType) {
        if (value == null || value.getClass().getTypeName().equals(type)) {
            return value;
        }
        return JSON.parseObject(JSON.toJSONString(value), genericType == null ? type : genericType);
    }

}
