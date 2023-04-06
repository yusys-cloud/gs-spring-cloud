package com.example.seata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangzq80@gmail.com
 * @date 2/8/23
 */
@Slf4j
@Service
@LocalTCC
public class TCCService {

    @Autowired
    UserRepository userRepository;

    @TwoPhaseBusinessAction(name = "TccActionB", commitMethod = "commit", rollbackMethod = "rollback")
    public User prepare(@BusinessActionContextParameter("user") User user) {
        log.info("Distributed --- Transaction --- tcc-try ... xid: {}", RootContext.getXID());

        if (user.money == 200) {
            log.error("svc-b user.money=200 error :{}", user);
            throw new RuntimeException("svc-b user.money=200 error");
        }
        return user;
    }

    public void commit(BusinessActionContext actionContext) {

        User user = JSON.toJavaObject((JSONObject) actionContext.getActionContext("user"), User.class);

        userRepository.save(user);

        log.info("Distributed --- Transaction --- tcc-commit ... xid: {}  Parameters --- : {}", actionContext.getXid(), actionContext.getActionContext("user"));
    }

    public void rollback(BusinessActionContext actionContext) {

        User user = JSON.toJavaObject((JSONObject) actionContext.getActionContext("user"), User.class);

        userRepository.delete(user);

        log.info("Distributed --- Transaction --- tcc-cancel ... xid: {}  Parameters --- : {}", actionContext.getXid(), actionContext.getActionContext("user"));
    }
}
