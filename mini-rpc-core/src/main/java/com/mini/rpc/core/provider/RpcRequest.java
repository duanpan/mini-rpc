package com.mini.rpc.core.provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author dp
 * @Date 2024/3/7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class RpcRequest {
    /**
     * 服务签名
     **/
    private String serviceSign;
    /**
     * 参数
     **/
    private Object[] args;

    /**
     * 扩展参数
     */
    private Map<String,String> params=new HashMap();

}
