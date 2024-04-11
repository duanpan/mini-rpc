package com.mini.rpc.test.provider.api;

import java.util.List;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/4/7
 */
public interface TypeCastService {


    Integer integerCast(Integer obj);

    Long longCast(Long obj);

    Double doubleCast(Double obj);

    List<Integer> listCast(List<Integer> obj);

    Integer[] arrayCast(Integer[] obj);

    User[] arrayObjCast(User[] obj);

    List<User> listObjCast(List<User> obj);

    Map<String, Long> mapCast(Map<String, Long> obj);

    Map<String, User> mapObjCast(Map<String, User> obj);

    Map<String, List<User>> mapListObjCast(Map<String, List<User>> obj);

}
