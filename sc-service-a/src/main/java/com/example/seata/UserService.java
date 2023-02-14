package com.example.seata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yangzq80@gmail.com
 * @date 2/8/23
 */
@Slf4j
@Service
public class UserService {

    public boolean createUser(String businessKey, User user) {

        log.info("saga ---- svc-a ----- create user: {}", user);
        if (user.money == 100) {
            log.error("svc-a user.money=100 error :{}", user);
//            throw new RuntimeException("svc-a user.money=100 error");
            return false;
        }
        user.setId(1L);
        return true;
    }

    public boolean deleteUser(String businessKey) {
        log.info("saga ---- svc-a ----- compensate ----- create key: {}", businessKey);
        return true;
    }
}
