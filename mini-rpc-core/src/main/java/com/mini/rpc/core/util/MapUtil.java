package com.mini.rpc.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/3/25
 */
public class MapUtil {


    public static <K, V> void addMultiValue(Map<K, List<V>> multiMap, K key, V value) {
        if (multiMap.containsKey(key)) {
            multiMap.get(key).add(value);
        } else {
            multiMap.put(key, new ArrayList<>(Arrays.asList(value)));
        }
    }

    public static <K, V> void addMultiValue(Map<K, List<V>> multiMap, K key, List<V> values) {
        if (multiMap.containsKey(key)) {
            multiMap.get(key).addAll(values);
        } else {
            multiMap.put(key, values);
        }
    }

}
