package com.example.seata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author yangzq80@gmail.com
 * @date 2/1/23
 */
@RestController
@RequestMapping("/v1/seata")
public class SeataController {

    @Autowired
    BusinessService businessService;

    @Autowired
    SagaService sagaService;

    @PostMapping("/testAT")
    public ResponseEntity<Object> testAT(@RequestBody User user) {
        Map entity = businessService.testAT(user);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PostMapping("/testTCC")
    public ResponseEntity<Object> testTCC(@RequestBody User user) {
        Map entity = businessService.testTCC(user);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @PostMapping("/testSaga")
    public ResponseEntity<Object> testSaga(@RequestBody User user) {
        Object entity = sagaService.testSaga(user);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }


    long i = 0L;

    @RequestMapping("/auto")
    public ResponseEntity<Object> auto(@RequestParam("type") String type) {
        i++;
        Object entity = null;
        if ("at" == type) {
            User user = new User();
            user.setId(i);
            entity = businessService.testAT(user);
        }

        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }
}
