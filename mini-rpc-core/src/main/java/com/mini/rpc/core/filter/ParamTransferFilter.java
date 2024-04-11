package com.mini.rpc.core.filter;

import com.mini.rpc.core.holder.RpcRequestHolder;
import com.mini.rpc.core.provider.RpcRequest;
import com.mini.rpc.core.provider.RpcResponese;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 扩展参数传递
 *
 * @Author dp
 * @Date 2024/4/11
 */
public class ParamTransferFilter implements Filter {

    @Override
    public void pre(RpcRequest request) {
        if(ObjectUtils.isNotEmpty(RpcRequestHolder.getAll())){
            request.getParams().putAll(RpcRequestHolder.getAll());
        }
    }

    @Override
    public void post(RpcRequest request, RpcResponese responese) {

    }
}
