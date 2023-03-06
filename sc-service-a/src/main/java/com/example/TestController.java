package com.example;

import com.example.seata.User;
import com.example.seata.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
@RestController
@Slf4j
public class TestController {
    @Resource
    SvcBClient svcBClient;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/api/sc/a")
    public String apiA() {
        return "sc-service-a UP call sc-service-b --> " + svcBClient.apiB();
    }

    @GetMapping("/user")
    public List<User> getUsers() {
//        ZoneAvoidanceRule zoneAvoidanceRule;
//        DynamicServerListLoadBalancer dynamicServerListLoadBalancer;
        log.info("get user--->");
        return svcBClient.getUsers();
    }

    @GetMapping("/db/user")
    public List<User> users() {
        return userRepository.findAll();
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
