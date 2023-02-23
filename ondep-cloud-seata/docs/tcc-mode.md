# TCC 模式

TCC 模式，不依赖于底层数据资源的事务支持：
- 一阶段 prepare 行为：调用 自定义 的 prepare 逻辑。
- 二阶段 commit 行为：调用 自定义 的 commit 逻辑。
- 二阶段 rollback 行为：调用 自定义 的 rollback 逻辑。

TCC 分布式事务模型需要业务系统提供三段业务逻辑：

1. 初步操作 Try：完成所有业务检查，预留必须的业务资源。
2. 确认操作 Confirm：真正执行的业务逻辑，不做任何业务检查，只使用 Try 阶段预留的业务资源。因此，只要 Try 操作成功，Confirm 必须能成功。另外，Confirm 操作需满足幂等性，保证一笔分布式事务能且只能成功一次。
3. 取消操作 Cancel：释放 Try 阶段预留的业务资源。同样的，Cancel 操作也需要满足幂等性。

## 适用场景

TCC模式是高性能分布式事务解决方案，适用于核心系统等对性能有很高要求的场景。
TCC 模式适用于需要在分布式环境中控制事务一致性的场景，例如订单系统、支付系统等；金融核心系统使用，金融核心系统的特点是一致性要求高（业务上的隔离性）、短流程、并发高。
TCC 模式与其他事务模型相比，更适合需要实现高可用的场景。在 TCC 模式中，需要实现三个方法，即 try、confirm 和 cancel。事务的执行流程可以大致概括为：先执行 try 方法，进行业务操作前的预处理；再执行 confirm 方法，确认事务是否真正提交；如果发生异常，则执行 cancel 方法，回滚事务。

## 使用步骤

1. 应用增加pom依赖

``` 
    <dependencies>
        <dependency>
            <groupId>com.zjtlcb.one</groupId>
            <artifactId>ondep-cloud-seata</artifactId>
            <version>V4.0.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
```

2. 入口业务代码增加全局式事务注解

   在需要分布式事务的服务类上添加 @GlobalTransactional 注解。
   
```
   @GlobalTransactional
   public Object testDT(User user) {
       log.info("Distributed Transaction begin ... xid: " + RootContext.getXID());
   
       Map map = new HashMap();
   
       map.put("svc-a", userService.prepare(user));
   
       map.put("svc-b", svcBClient.create(user));
   
       map.put("svc-c", svcCClient.create(user));
   
       return map;
   }
   
```   

3. 子事务实现TCC相关方法
   子事务需加@LocalTCC 与 @TwoPhaseBusinessAction 两个注解，其中name需唯一； 实现 TCC 相关的三个方法：try、confirm 和 cancel。仅需调用try方法即可，commit与rollback方法seata-server协调调用
   
```
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
   
```

4. 使用注意:

   1. FeignClient返回对象为null时候会导致异常但不提示，直接调用rollback方法
   2. TCC方法中参数传递: try方法为业务调用入口方法，commit与cancel方法无参数传统，需从 BusinessActionContext 中取值准备方法prepare中通过@BusinessActionContextParameter 注解传递参数