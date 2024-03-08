package com.mini.rpc.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponese {
    /**
     * 响应状态
     **/
    private Boolean sucess;
    /**
     * 响应数据
     **/
    private Object data;
}
