package com.mini.rpc.core.route;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * @Author dp
 * @Date 2024/3/21
 */
public class ConfRouter implements Router {

    private Environment environment;


    public ConfRouter(Environment environment) {
        this.environment = environment;
    }


    @Override
    public List<String> getRoute(String serviceSign) {
        String roteUrls = environment.getProperty("minirpc.provider.url");
        if (StringUtils.isBlank(roteUrls)) {
            return null;
        }
        String[] split = roteUrls.split(",");
        return Arrays.asList(split);
    }
}
