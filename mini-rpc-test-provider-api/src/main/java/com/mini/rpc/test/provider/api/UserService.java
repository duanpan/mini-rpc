package com.mini.rpc.test.provider.api;

import java.util.List;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/3/7
 */
public interface UserService {

    User getUser(Integer userId, String userName);

    User getUser(User user);

    User getById(Integer userId);

    User list();

    User timeOut();

    User list(Integer userId);

    List<User> list(List<User> userList);

    int[] list(int[] userIdArrat);

    Map<String,String> list(Map<String,String>  map);
}
