package com.example.seata;

import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yangzq80@gmail.com
 * @date 2/8/23
 */
@Slf4j
@Service
@LocalTCC
public class UserService {

    @TwoPhaseBusinessAction(name = "TccActionA", commitMethod = "commit", rollbackMethod = "rollback")
    public User prepare(@BusinessActionContextParameter("user") User user) {
        log.info("Distributed --- Transaction --- tcc-try ... xid: {}", RootContext.getXID());
        user.setId(1L);
        return user;
    }

    public void commit(BusinessActionContext actionContext) {
        log.info("Distributed --- Transaction --- tcc-commit ... xid: {}  Parameters --- : {}", actionContext.getXid(), actionContext.getActionContext("user"));
    }

    public void rollback(BusinessActionContext actionContext) {

        log.info("Distributed --- Transaction --- tcc-cancel ... xid: {}  Parameters --- : {}", actionContext.getXid(), actionContext.getActionContext("user"));
    }
}
