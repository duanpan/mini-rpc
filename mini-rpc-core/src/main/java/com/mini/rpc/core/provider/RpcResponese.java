package com.mini.rpc.core.provider;

import com.mini.rpc.core.enums.ResultCode;
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

    public RpcResponese(Object data) {
        this.sucess = true;
        this.code = ResultCode.SUCCESS.getCode();
        this.data = data;
        this.msg=ResultCode.SUCCESS.getMsg();
    }

    public RpcResponese(ResultCode resultCode, Exception ex) {
        this.sucess = false;
        this.code = resultCode.getCode();
        this.msg=resultCode.getMsg();
        this.ex = ex;
    }

    /**
     * 响应状态
     **/
    private Boolean sucess;
    /**
     * 响应状态码
     */
    private String code;
    /**
     * 响应数据
     **/
    private Object data;
    /**
     * 响应消息
    */
    private String msg;
    /**
     * 异常信息
     */
    private Exception ex;

}
