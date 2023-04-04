package com.example.feign;

import feign.Client;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangzq80@gmail.com
 * @date 3/10/23
 */
//@Configuration
public class MyClient {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate ->
                requestTemplate.query("url", "http://my-service/v2");
    }
}
