package com.mini.rpc.core.provider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @Author dp
 * @Date 2024/3/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class RpcRequest {
    /**
     * 服务签名
     **/
    private String serviceSign;
    /**
     * 参数
     **/
    private Object[] args;

}
