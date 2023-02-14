package com.example.seata;

import com.example.SvcBClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author yangzq80@gmail.com
 * @date 2/14/23
 */
@Service
@Slf4j
public class SvcBUserService {

    @Autowired
    SvcBClient svcBClient;
    public boolean reduce(String businessKey, BigDecimal amount, Map<String, Object> params) {
        if(params != null) {
            Object throwException = params.get("throwException");
            if (throwException != null && "true".equals(throwException.toString())) {
                throw new RuntimeException("reduce balance failed");
            }
        }
        if (amount.compareTo(BigDecimal.valueOf(100))==0){
            throw new RuntimeException("svc-a money=100 error");
        }
        log.info("svc-b ------ reduce balance succeed, amount: " + amount + ", businessKey:" + businessKey);
        return true;
    }

    public boolean compensateReduce(String businessKey, Map<String, Object> params) {
        if(params != null) {
            Object throwException = params.get("throwException");
            if (throwException != null && "true".equals(throwException.toString())) {
                throw new RuntimeException("compensate reduce balance failed");
            }
        }
        log.info("svc-b *** compensate *** reduce balance succeed, businessKey:" + businessKey);
        return true;
    }

}
