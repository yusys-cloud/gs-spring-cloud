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

    @Autowired
    UserService userService;

    public User save(User user) {

        return userService.prepare(user);
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