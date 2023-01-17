package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
@SpringBootApplication
@EnableFeignClients
public class MainApp {
    public static void main(String[] args) {
//        System.setProperty("spring.devtools.restart.enabled", "true");
        System.setProperty("spring.devtools.remote.secret", "mysecret");
        SpringApplication.run(MainApp.class, args);
    }
}
