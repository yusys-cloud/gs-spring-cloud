package com.example.feign;

import feign.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author yangzq80@gmail.com
 * @date 3/10/23
 */
@Configuration
public class CustomerFeign {

    Retryer retryer = new Retryer.Default();

    @Bean
    public Feign.Builder feignBuilder() {

        System.out.println("===");
        return Feign.builder().client(new MyClient()).retryer(retryer);
    }

    class MyClient implements Client {
        @Override
        public Response execute(Request request, Request.Options options) throws IOException {

            System.out.println(request.url());
            return null;
        }

    }

}
