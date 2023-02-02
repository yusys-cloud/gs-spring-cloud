package com.example.seata;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yangzq80@gmail.com
 * @date 1/31/23
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
