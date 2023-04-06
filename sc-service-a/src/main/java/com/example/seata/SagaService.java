package com.example.seata;

import com.example.SvcBClient;
import com.example.SvcCClient;
import io.seata.core.context.RootContext;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.StateMachineInstance;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(name = "saga.enabled", matchIfMissing = true)
public class SagaService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    SvcBClient svcBClient;
    @Autowired
    SvcCClient svcCClient;

    @Autowired
    StateMachineEngine stateMachineEngine;


    @GlobalTransactional
    public Object testSaga(User user) {
        Map map = new HashMap();

        Map<String, Object> startParams = new HashMap<>(1);
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("user", user);
        startParams.put("businessKey", businessKey);

        StateMachineInstance inst = stateMachineEngine.startWithBusinessKey("dbStateMachineApp", null, businessKey,startParams);

        log.info("saga transaction commit ****succeed****. XID: " + inst.getId());

        return map;
    }
}