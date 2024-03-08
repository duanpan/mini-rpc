package com.mini.rpc.test.consumer.biz;

import com.mini.rpc.core.annotation.RpcConsumer;
import com.mini.rpc.test.provider.api.User;
import com.mini.rpc.test.provider.api.UserService;
import org.springframework.stereotype.Component;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@Component
public class SystemServiceImpl implements SystemService {

    @RpcConsumer
    private UserService userService;

    @Override
    public User getUser() {
        return userService.getById(1);
    }
}
