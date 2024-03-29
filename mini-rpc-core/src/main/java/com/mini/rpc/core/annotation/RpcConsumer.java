package com.mini.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface RpcConsumer {

}
