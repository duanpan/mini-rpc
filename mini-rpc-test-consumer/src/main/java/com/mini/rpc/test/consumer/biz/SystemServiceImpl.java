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

    public Object timeout() {
        try {
            User user = userService.timeOut();
            System.out.println(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
//
//        User user1 = userService.getUser(new User(2,"3"));
//        System.out.println(user1);
//
//        ArrayList<User> users=new ArrayList<>();
//        users.add(new User(1,"1"));
//        users.add(new User(2,"2"));
//        List<User> list = userService.list(users);
//        System.out.println(list);
//
//        int[] list1 = userService.list(new int[]{1, 2});
//        Arrays.stream(list1).forEach(System.out::println);
//
//        Map map=new HashMap<>();
//        map.put("1","2");
//        Map rmap = userService.list(map);
//        System.out.println(rmap);
//
//        User user3 = userService.list(3);
//        System.out.println(user3);
    }
}
