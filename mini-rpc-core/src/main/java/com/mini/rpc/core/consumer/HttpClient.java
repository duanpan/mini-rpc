package com.mini.rpc.core.consumer;

import java.io.IOException;
import java.util.Map;

/**
 * Http客户端
 *
 * @Author dp
 * @Date 2024/4/10
 */
public interface HttpClient {


    public Object  post(String url, String jsonParameter) throws IOException;

    public Object  get(String url, String jsonParameter) throws IOException;

}
