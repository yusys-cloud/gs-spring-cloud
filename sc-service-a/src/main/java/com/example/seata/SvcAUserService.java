package com.example.seata;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yangzq80@gmail.com
 * @date 2/8/23
 */
@Slf4j
@Service
public class SvcAUserService {

    public boolean reduce(String businessKey, int count) {
        log.info("svc-a ------ reduce inventory succeed, count: " + count + ", businessKey:" + businessKey);
        return true;
    }

    public boolean compensateReduce(String businessKey) {
        log.info("svc-a  *** compensate ***  reduce inventory succeed, businessKey:" + businessKey);
        return true;
    }
}
