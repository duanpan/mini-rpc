package com.mini.rpc.test.consumer.controller;

import com.mini.rpc.test.consumer.biz.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author dp
 * @Date 2024/5/6
 */
@Slf4j
@RestController
@RequestMapping("/rpc")
public class TestRpcController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/test/timeout")
    public Object test() {
       return systemService.timeout();
    }


}
