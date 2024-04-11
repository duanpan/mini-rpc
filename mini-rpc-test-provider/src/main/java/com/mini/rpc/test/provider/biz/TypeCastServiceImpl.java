package com.mini.rpc.test.provider.biz;

import com.mini.rpc.core.annotation.RpcProvider;
import com.mini.rpc.test.provider.api.TypeCastService;
import com.mini.rpc.test.provider.api.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author dp
 * @Date 2024/4/7
 */
@RpcProvider
@Component
public class TypeCastServiceImpl  implements TypeCastService {

    @Override
    public Integer integerCast(Integer obj) {
        return obj;
    }

    @Override
    public Long longCast(Long obj) {
        return obj;
    }

    @Override
    public Double doubleCast(Double obj) {
        return obj;
    }

    @Override
    public List<Integer> listCast(List<Integer> obj) {
        return obj;
    }

    @Override
    public Integer[] arrayCast(Integer[] obj) {
        return obj;
    }

    @Override
    public User[] arrayObjCast(User[] obj) {
        return obj;
    }

    @Override
    public List<User> listObjCast(List<User> obj) {
        return obj;
    }

    @Override
    public Map<String, Long> mapCast(Map<String, Long> obj) {
        return obj;
    }

    @Override
    public Map<String, User> mapObjCast(Map<String, User> obj) {
        return obj;
    }

    @Override
    public Map<String, List<User>> mapListObjCast(Map<String, List<User>> obj) {
        return obj;
    }
}
