package com.mini.rpc.core.enums;

/**
 * @Author dp
 * @Date 2024/4/12
 */
public enum ResultCode {

    SUCCESS("200","sucess"),
    ERROR_CLIENT("ES_400","客户端参数错误"),
    ERROR_SERVER("ES_500","服务端内部错误");

    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
