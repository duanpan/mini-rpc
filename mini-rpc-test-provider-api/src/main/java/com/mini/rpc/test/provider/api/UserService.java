package com.mini.rpc.test.provider.api;

/**
 * @Author dp
 * @Date 2024/3/7
 */
public interface UserService {

    User getUser(Integer userId,String userName);

    User getById(Integer userId);

    User list();

    User list(Integer userId);

}
