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
public class ScServiceB {
    public static void main(String[] args) {
        SpringApplication.run(ScServiceB.class,args);
    }
}
