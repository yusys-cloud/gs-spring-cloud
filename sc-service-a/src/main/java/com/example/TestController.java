package com.example;

import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
@RestController
@Slf4j
public class TestController {
    @Resource
    ScServiceBClient scServiceBClient;

    @GetMapping("/api/sc/a")
    public String apiA() {
        return "sc-service-a UP call sc-service-b --> " + scServiceBClient.apiB();
    }

    @GetMapping("/user")
    public List<User> getUsers() {
        ZoneAvoidanceRule zoneAvoidanceRule;
        DynamicServerListLoadBalancer dynamicServerListLoadBalancer;
        log.info("get user--->");
        return scServiceBClient.getUsers();
    }

    @GetMapping("/test/user")
    public List<User> users() {
        return Arrays.asList(new User(1L, "Trump"), new User(2L, "Donal2d"));
    }


    @GetMapping("/base")
    public String helloSleuth(String a) {
        if (a.equalsIgnoreCase("aa")) {
            throw new RuntimeException("base error");
        }
        log.info("Hello Sleuth");
        return "success";
    }
}
