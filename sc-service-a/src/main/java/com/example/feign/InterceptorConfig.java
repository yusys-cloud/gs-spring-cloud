package com.example.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author yangzq80@gmail.com
 * @date 3/21/23
 */
@Configuration
public class InterceptorConfig {

    @Autowired
    private Environment environment;

    @Bean
    public RequestInterceptor cloudContextInterceptor() {

        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {

                template.target("http://localhost:" + environment.getProperty("server.port", "8080"));

            }
        };
    }
}
