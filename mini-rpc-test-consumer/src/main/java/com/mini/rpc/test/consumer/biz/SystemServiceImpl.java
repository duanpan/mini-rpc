package com.mini.rpc.test.consumer.biz;

import com.mini.rpc.core.consumer.RpcConsumer;
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
    public void test() {
//        User user = userService.getUser(1,"2");
//        System.out.println(user);

//        User user1 = userService.getUser(new User(2,"3"));
//        System.out.println(user1);

        User user2 = userService.list();
        System.out.println(user2);
//
//        User user3 = userService.list(3);
//        System.out.println(user3);
    }
}
