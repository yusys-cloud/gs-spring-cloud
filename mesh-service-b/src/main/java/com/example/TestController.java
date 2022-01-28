package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
@RestController
@Slf4j
public class TestController {

    private static String SPRING_CLOUD_GATEWAY = "http://localhost";

    @GetMapping("/api/sc/b")
    public String apiB() {
        return "[Mesh-service-b UP] response ok call -> " + restTemplate.getForObject(SPRING_CLOUD_GATEWAY + "/sc-service-c/api/sc/c", String.class);
    }

    @Autowired
    RestTemplate restTemplate;

    @Bean
    RestTemplate newRestTemplate() {
        return new RestTemplate();
    }

}
