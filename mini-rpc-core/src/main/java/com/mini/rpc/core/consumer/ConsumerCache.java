package com.mini.rpc.core.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author dp
 * @Date 2024/3/25
 */
public class ConsumerCache {

    public static ConcurrentHashMap<String, List<String>> providesOnline= new ConcurrentHashMap();

    public static Map<String,Object> proxyCache=new HashMap();

}
