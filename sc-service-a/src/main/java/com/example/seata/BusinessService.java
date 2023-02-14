package com.example.seata;

import com.example.SvcBClient;
import com.example.SvcCClient;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    @Autowired
    StateMachineEngine stateMachineEngine;

    public Object testDT(User user) {
        Map map = new HashMap();

        Map<String, Object> startParams = new HashMap<>(1);
        String businessKey = String.valueOf(System.currentTimeMillis());
        startParams.put("user", user);
        startParams.put("businessKey", businessKey);

        StateMachineInstance inst = stateMachineEngine.start("testSagaSvcs", null, startParams);

//        Assert.isTrue(ExecutionStatus.SU.equals(inst.getStatus()), "saga transaction execute failed. XID: " + inst.getId());
//        log.info("saga transaction commit ****succeed****. XID: " + inst.getId());

        return map;
    }

    public User save(User user) {
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