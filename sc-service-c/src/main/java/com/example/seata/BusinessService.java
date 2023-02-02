package com.example.seata;

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

        if (user.money == 300) {
            throw new RuntimeException("svc-c user.money=300 error");
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