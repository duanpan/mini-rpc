package com.mini.rpc.core.registry;

import com.mini.rpc.core.constans.RpcConstans;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * 配置文件注册中心
 *
 * @Author dp
 * @Date 2024/3/23
 */
@Data
public class SaticConfigRegistryCenter implements RegistryCenter {

    private final Environment environment;


    @Override
    public List<String> fetchServer(String serviceSign) {
        String roteUrls = environment.getProperty(RpcConstans.CONFIG_REGISTRY_HOST_ENV);
        if (StringUtils.isNotBlank(roteUrls)) return null;
        return Arrays.asList(roteUrls.split(","));
    }
}
