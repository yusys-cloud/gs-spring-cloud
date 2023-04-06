package com.example.seata;

import com.example.SvcBClient;
import com.example.SvcCClient;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author yangzq80@gmail.com
 * @date 2/1/23
 */
@Service
@Slf4j
public class BusinessService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SvcBClient svcBClient;
    @Autowired
    SvcCClient svcCClient;

    public User save(User user) {
        log.debug("save user:{}", user);
        return userRepository.save(user);
    }


    public User findByID(Long id) {

        Optional<User> current = userRepository.findById(id);
        if (!current.isPresent()) {
            return null;
        }
        return current.get();
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @GlobalTransactional
    public Map testAT(User user) {
        log.debug("Distributed-----Transaction-----AT------- begin ... xid: " + RootContext.getXID());

        Map map = new HashMap();

        map.put("svc-a", save(user));

        map.put("svc-b", svcBClient.create(user));

        return map;
    }


    @GlobalTransactional
    public Map testTCC(User user) {
        log.debug("Distributed-----Transaction-----TCC------- begin ... xid: " + RootContext.getXID());

        Map map = new HashMap();

        map.put("svc-a", save(user));

        map.put("svc-b", svcBClient.tcc(user));

        return map;
    }
}