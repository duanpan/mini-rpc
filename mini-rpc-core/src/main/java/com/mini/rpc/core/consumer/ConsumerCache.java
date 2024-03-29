package com.mini.rpc.core.consumer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author dp
 * @Date 2024/3/25
 */
public class ConsumerCache {

    public static ConcurrentHashMap<String, List<String>> providesOnline= new ConcurrentHashMap();

}
