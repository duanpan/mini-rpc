package com.mini.rpc.core.registry;

import com.mini.rpc.core.constans.RpcConstans;
import com.mini.rpc.core.entity.ProviderInstance;
import com.mini.rpc.core.entity.ServiceMeta;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ProviderInstance> fetchServer(ServiceMeta serviceMeta) {
        String roteUrls = environment.getProperty(RpcConstans.REGISTRY_HOST_ENV);
        if (StringUtils.isNotBlank(environment.getProperty(RpcConstans.REGISTRY_HOST_ENV))) {
            return null;
        }
        String[] urls = roteUrls.split(",");
        return Arrays.asList(urls).stream().map(url -> {
            ProviderInstance instance = new ProviderInstance();
            instance.setIp(url.substring(0, url.indexOf(":")));
            instance.setPort(url.substring(url.indexOf(":")+1, url.length()));
            instance.setProtocol(serviceMeta.getProtocol());
            return instance;
        }).collect(Collectors.toList());
    }
}
