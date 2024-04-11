package com.mini.rpc.core.consumer;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @Author dp
 * @Date 2024/4/10
 */
public class MyOkHttpClient implements HttpClient{

    OkHttpClient client;

    public MyOkHttpClient() {
        ConnectionPool connectionPool = new ConnectionPool(200, 20, TimeUnit.MINUTES);
         client = new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .build();
    }

    @Override
    public Object post(String url, String parameter) throws IOException {
        RequestBody requestBody = RequestBody.create(parameter, MediaType.parse("application/json"));
        OkHttpClient client = new OkHttpClient();
        Request httpRequest = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(httpRequest).execute();
        return response.body().string();
    }


    @Override
    public Object get(String url, String parameter) throws IOException {
        return null;
    }
}
