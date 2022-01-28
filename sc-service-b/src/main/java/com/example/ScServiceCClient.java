package com.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author : yangzq80@gmail.com
 * @date: 2019-05-06
 */
@FeignClient("sc-service-c")
public interface ScServiceCClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/sc/c")
    String apiC();

}
