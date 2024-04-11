package com.mini.rpc.core.filter;

import com.mini.rpc.core.provider.RpcRequest;
import com.mini.rpc.core.provider.RpcResponese;

/**
 * 请求执行前过滤器
 *
 * @Author dp
 * @Date 2024/4/11
 */
public interface Filter {

    public void pre(RpcRequest request);


    public void post(RpcRequest request, RpcResponese responese);
}
