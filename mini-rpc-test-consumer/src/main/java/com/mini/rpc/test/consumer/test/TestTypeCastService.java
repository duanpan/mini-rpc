package com.mini.rpc.test.consumer.test;

import com.google.common.collect.Lists;
import com.mini.rpc.core.annotation.RpcConsumer;
import com.mini.rpc.test.provider.api.TypeCastService;
import com.mini.rpc.test.provider.api.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/4/7
 */
@Component
public class TestTypeCastService {

    @RpcConsumer
    private TypeCastService typeCastService;


    public void typeCast() {
//        Integer integer = typeCastService.integerCast(1);
//        System.out.println("integerCast:" + integer);
//
//        Long aLong = typeCastService.longCast(2L);
//        System.out.println("longCast:" + aLong);
//
//        Double aDouble = typeCastService.doubleCast(3.3);
//        System.out.println("doubleCast:" + aDouble);
//
//        Integer[] integerArray = new Integer[]{5, 5};
//        Integer[] integerArrayR = typeCastService.arrayCast(integerArray);
//        for (Integer integer1 : integerArrayR) {
//            System.out.println("arrayCast:" + integer1);
//        }
//
//        User user = new User(6, "6-6");
//        User[] userArray = new User[]{user};
//        User[] userArrayR = typeCastService.arrayObjCast(userArray);
//        for (User user1 : userArrayR) {
//            System.out.println("arrayObjCast:" + user1.toString());
//        }

//        List<Integer> integers = Lists.newArrayList(7, 77);
//        List<Integer> integersR = typeCastService.listCast(integers);
//        System.out.println("listCast:" + integersR);

        User user8 = new User(8, "8-8");
        List<User> userList =new ArrayList();
        userList.add(user8);
        List<User> userListR = typeCastService.listObjCast(userList);
        for (User user1 : userListR) {
            System.out.println("listObjCast:"+user1.toString());
        }
        System.out.println(userListR);

        Map<String, Long> map = new HashMap<>();
        map.put("9", 99L);
        Map<String, Long> mapR = typeCastService.mapCast(map);
        System.out.println("mapCast:" + mapR);

        Map<String, User> mapU = new HashMap<>();
        User user10 = new User(10, "10-10");
        mapU.put("9", user10);
        Map<String, User> stringUserMap = typeCastService.mapObjCast(mapU);
        System.out.println("mapObjCast:" + stringUserMap);

        Map<String, List<User>> mapL = new HashMap<>();
        User user11 = new User(11, "11-11");
        List<User> userList11 = Lists.newArrayList(user11);
        mapL.put("11", userList11);
        Map<String, List<User>> stringListMapR = typeCastService.mapListObjCast(mapL);
        System.out.println("mapListObjCast:" + stringListMapR);
    }

}
