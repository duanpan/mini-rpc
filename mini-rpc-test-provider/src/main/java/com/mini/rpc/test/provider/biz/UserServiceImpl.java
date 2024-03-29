package com.mini.rpc.test.provider.biz;

import com.mini.rpc.core.annotation.RpcProvider;
import com.mini.rpc.test.provider.api.User;
import com.mini.rpc.test.provider.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/3/7
 */
@RpcProvider
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private Environment environment;

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
        return new User(userId, userName+"-"+environment.getProperty("server.port"));
    }

    @Override
    public User list(Integer userId) {
        return new User(userId, "list userId");
    }

    @Override
    public User getUser(User user) {
        return user;
    }

    @Override
    public List<User> list(List<User> userList) {
        return userList;
    }

    @Override
    public int[] list(int[] userIdArrat) {
        return userIdArrat;
    }

    @Override
    public Map<String, String> list(Map<String, String> map) {
        return map;
    }
}
