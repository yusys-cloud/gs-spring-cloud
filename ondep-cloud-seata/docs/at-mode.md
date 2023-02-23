# AT 模式
AT模式即Automtic Trascation，也就是自动事务模式。AT模式是一种简单的事务模式，它的原理是两阶段提交协议
- 一阶段 prepare 行为：在本地事务中，一并提交业务数据更新和相应回滚日志记录。
- 二阶段 commit 行为：马上成功结束，自动 异步批量清理回滚日志。
- 二阶段 rollback 行为：通过回滚日志，自动 生成补偿操作，完成数据回滚。
AT 模式下，把每个数据库被当做是一个  Resource，Seata 里称为 DataSource Resource。业务通过 JDBC 标准接口访问数据库资源时，Seata 框架会对所有请求进行拦截，
做一些操作。每个本地事务提交时，Seata RM（Resource Manager，资源管理器） 都会向 TC（Transaction Coordinator，事务协调器） 注册一个分支事务。当请求链路调用完成后，发起方通知 TC 提交或回滚分布式事务，进入二阶段调用流程。此时，TC 会根据之前注册的分支事务回调到对应参与者去执行对应资源的第二阶段。每个资源都有一个全局唯一的资源 ID，并且在初始化时用该 ID 向 TC 注册资源。在运行时，每个分支事务的注册都会带上其资源 ID。这样 TC 就能在二阶段调用时正确找到对应的资源。
## 适用场景
AT 模式基于 支持本地 ACID 事务 的 关系型数据库,适用于：

- 基于支持本地 ACID 事务的关系型数据库。
- Java 应用，通过 JDBC 访问数据库。

不支持复杂SQL及嵌套、不支持replace、truncate 语句、统计函数不支持。
AT模式适用于多个参与者节点必须全部操作事务，或者全部退出事务。它给用户提供了在可靠性和性能之间做出折衷的机会。但它不能满足多个参与者节点可以部分操作事务，部分退出事务的场景。

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

2. 业务代码中增加分布式事务注解
在要使其具有事务性的服务入口方法上添加 @GlobalTransactional 注释，启用全局事务

如 : 本地子事务svc-a与远程feignclient调用子事务svc-b、svc-c 共同组成一个分布式事务 ,任何一个子事务失败整个事务提交失败

``` 
@GlobalTransactional
public Object testDT(User user) {
    log.info("Distributed Transaction begin ... xid: " + RootContext.getXID());

    Map map = new HashMap();

    map.put("svc-a", save(user));

    map.put("svc-b", svcBClient.create(user));

    map.put("svc-c", svcCClient.create(user));

    return map;
}
```

3. 应用DB库新建事务回滚表

SEATA 中 undo_log 表的作用是记录事务回滚时需要撤销的数据操作，以便在事务回滚时对数据进行恢复。

```
CREATE TABLE `undo_log`
(
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `branch_id`     BIGINT(20)   NOT NULL,
  `xid`           VARCHAR(100) NOT NULL,
  `context`       VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB     NOT NULL,
  `log_status`    INT(11)      NOT NULL,
  `log_created`   DATETIME     NOT NULL,
  `log_modified`  DATETIME     NOT NULL,
  `ext`           VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
```