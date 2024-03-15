package com.mini.rpc.core.util;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/15
 */
public class TypeUtil {


    public static Object cast(Object value, Type type) {
        if (value == null) {
            return value;
        }
        if (value.getClass().getTypeName().equals(type.getTypeName())) {
            return value;
        }

        String oldValue = value.toString();
        Object newValue = null;
        if (type == Integer.TYPE || type == Integer.class) {
            newValue = Integer.parseInt(oldValue);
        } else if (type == Long.TYPE || type == Long.class) {
            newValue = Long.parseLong(oldValue);
        } else if (type == Float.TYPE || type == Float.class) {
            newValue = Float.parseFloat(oldValue);
        } else if (type == Double.TYPE || type == Double.class) {
            newValue = Double.parseDouble(oldValue);
        } else if (type == Boolean.TYPE || type == Boolean.class) {
            newValue = Boolean.parseBoolean(oldValue);
        } else if (type == Character.TYPE || type == Character.class) {
            newValue = oldValue.charAt(0);
        } else if (type == String.class) {
            newValue = oldValue;
        } else if (type == HashMap.class) {
            newValue = JSON.toJSON(value);
        } else if (type.getClass().isArray()) {
            Class<? extends Type> aClass = type.getClass();
            Type componentType = aClass.getComponentType();
            int length = Array.getLength(value);
            Object dataArray = Array.newInstance(componentType.getClass(), length);
            for (int i = 0; i < length; i++) {
                Array.set(dataArray, i, cast(Array.get(value, i), componentType));
            }
            newValue = dataArray;
        } else if (type instanceof List) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType.getTypeName().equals("java.util.List")) {
                Type elementType = parameterizedType.getActualTypeArguments()[0];
                if (value instanceof List) {
                    List<?> listValue = (List<?>) value;
                    List<Object> resultList = new ArrayList<>();
                    for (Object element : listValue) {
                        Object castedElement = cast(element, elementType);
                        resultList.add(castedElement);
                    }
                    newValue = resultList;
                }
            }
        } else {
            newValue = JSON.parseObject(JSON.toJSONString(value), type);
        }
        return newValue;
    }
}
