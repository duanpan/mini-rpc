package com.mini.rpc.test.provider.biz;

import com.mini.rpc.core.provider.RpcProvider;
import com.mini.rpc.test.provider.api.User;
import com.mini.rpc.test.provider.api.UserService;
import org.springframework.stereotype.Component;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@RpcProvider
@Component
public class UserServiceImpl implements UserService {

    @Override
    public User getById(Integer userId) {
        return new User(userId, "rpc" + userId);
    }

    @Override
    public User list() {
        return new User(666, "rpc-list");
    }

    @Override
    public User getUser(Integer userId, String userName) {
        return new User(userId, userName);
    }

    @Override
    public User list(Integer userId) {
        return new User(userId, "list userId");
    }

    @Override
    public User getUser(User user) {
        return user;
    }
}
