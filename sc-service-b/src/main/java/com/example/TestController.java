package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
@RestController
@Slf4j
public class TestController {
    @Resource
    SvcCClient svcCClient;

    @GetMapping("/api/sc/b")
    public String apiB() {
        return "sc-service-b UP response ok call -> " + svcCClient.apiC();
    }
}
