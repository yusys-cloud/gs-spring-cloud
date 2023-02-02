package com.example;

import com.example.seata.User;
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
public interface SvcCClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/user")
    List<User> getUsers();

    @RequestMapping(method = RequestMethod.PUT, value = "/v1/user/{userId}", consumes = "application/json")
    User update(@PathVariable("userId") Long userId, User user);

    @RequestMapping(method = RequestMethod.POST, value = "/v1/user")
    User create(User user);
}