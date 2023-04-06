package com.example.seata;

import com.example.SvcBClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangzq80@gmail.com
 * @date 2/14/23
 */
@Service
@Slf4j
public class SvcBUserService {

    @Autowired
    SvcBClient svcBClient;

    public boolean createUser(String businessKey, User user) {

        log.debug("saga ---- svc-b ----- create user: {}", user);
        svcBClient.create(user);
        if (user.money==200){
            log.error("svc-b money 200 error");
            return false;
        }
        return true;
    }

    public boolean deleteUser(String businessKey) {
        log.debug("saga ---- svc-b ----- compensate ----- create key: {}", businessKey);
        return true;
    }

}
