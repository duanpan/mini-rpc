package com.mini.rpc.core.holder;

import java.util.Map;

/**
 * 请求参数本地线程变量
 *
 * @Author dp
 * @Date 2024/4/11
 */
public class RpcRequestHolder {

    private static ThreadLocal<Map<String, String>> paramLocal = new ThreadLocal<>();


    public static void addParam(String paramKey, String paramValue) {
        paramLocal.get().put(paramKey, paramValue);
    }

    public static void addAll(Map<String, String> params) {
        paramLocal.get().putAll(params);
    }

    public static Map<String, String> getAll() {
        return paramLocal.get();
    }

    public static String getParam(String paramKey) {
        return paramLocal.get().get(paramKey);
    }

}
