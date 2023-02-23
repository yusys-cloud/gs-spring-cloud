package com.example.seata;

import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    public User save(User user) {

        log.info("Distributed-----Transaction----------- begin ... xid: " + RootContext.getXID());

        if (user.money == 200) {
            log.error("svc-b user.money=200 error :{}", user);
            throw new RuntimeException("svc-b user.money=200 error");
        }

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
}