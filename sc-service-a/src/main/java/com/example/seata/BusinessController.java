package com.example.seata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author yangzq80@gmail.com
 * @date 2/1/23
 */
@RestController
@RequestMapping("/v1/user")
public class BusinessController {

    @Autowired
    BusinessService businessService;

    @PostMapping("/testDT")
    public ResponseEntity<Object> test(@RequestBody User user) {
        Object entity = businessService.testDT(user);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User entity = businessService.save(user);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> read(@PathVariable Long id) {
        User entity = businessService.findByID(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        User current = businessService.findByID(id);
        if (current == null) {
            return ResponseEntity.notFound().build();
        }
        user.setId(id);
        User updated = businessService.save(user);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        businessService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
