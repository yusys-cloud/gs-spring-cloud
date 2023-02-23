# SAGA 模式
saga模式是长事务解决方案，适用于业务流程长且需要保证事务最终一致性的业务系统。
Saga模式是SEATA提供的长事务解决方案，在Saga模式中，业务流程中每个参与者都提交本地事务，当出现某一个参与者失败则补偿前面已经成功的参与者，一阶段正向服务和二阶段补偿服务都由业务开发实现。
## 适用场景

Saga是一种长事务的解决方案，更适合于业务流程长、业务流程多的场景；
- 业务流程长、业务流程多
- 参与者包含其它公司或遗留系统服务，无法提供 TCC 模式要求的三个接口
如果服务参与者包含其他公司或遗留系统服务，此时无法提供TCC模式下的三个接口，那么就需要采用Saga；
典型的业务系统：金融机构对接系统（需要对接外部系统）、渠道整合（流程长）、分布式架构服务；
有数据隔离性问题，可能产生脏数据、更新丢失、模糊读取等问题

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

2. application中配置启动默认状态机应用

``` 
# 启用默认saga状态机应用 dbStateMachineApp 配置
onedp:
  cloud:
    saga:
      enabled: true
```

3. 业务代码开发补偿方法

业务代码与正常Service代码无区别，仅需在每个业务方法需开发一个对应的补偿方法，如：

``` 
public boolean createUser(String businessKey, User user) {
    log.info("saga ---- svc-b ----- create user: {}", user);
    svcBClient.create(user);
    return true;
}
public boolean deleteUser(String businessKey) {
    log.info("saga ---- svc-b ----- compensate ----- create key: {}", businessKey);
    return true;
}
```

4. 状态机可视化设计器编排事务流程

编排好事务流程后的.json 文件存放到源码: resources/statelang/目录下，状态机引擎会自动从 classpath*:statelang/*.json 读取。 

- 本地安装使用
``` 
$ git clone https://github.com/seata/seata.git
$ cd saga/seata-saga-statemachine-designer
$ npm install
$ npm start
```
- 在线使用: http://saga.yangzhiqiang.tech

状态机设计需专门学习后才能使用，如下示例可参考状态机设计[语法参考](https://seata.io/zh-cn/docs/user/saga.html#State-language-referance)

- 直接修改实例脚本

``` 

{
  "Name": "dbStateMachineApp",
  "Comment": "testSagaSvcs",
  "StartState": "ServiceA",
  "Version": "0.0.1",
  "States": {
    "ServiceA": {
      "Type": "ServiceTask",
      "ServiceName": "svcAUserService",
      "ServiceMethod": "createUser",
      "CompensateState": "CompensateSvcA",
      "Next": "ChoiceState",
      "Input": [
        "$.[businessKey]",
        "$.[user]"
      ],
      "Output": {
        "reduceInventoryResult": "$.#root"
      },
      "Status": {
        "#root == true": "SU",
        "#root == false": "FA",
        "$Exception{java.lang.Throwable}": "UN"
      }
    },
    "ChoiceState": {
      "Type": "Choice",
      "Choices": [
        {
          "Expression": "[reduceInventoryResult] == true",
          "Next": "ServiceB"
        }
      ],
      "Default": "Fail"
    },
    "ServiceB": {
      "Type": "ServiceTask",
      "ServiceName": "svcBUserService",
      "ServiceMethod": "createUser",
      "CompensateState": "CompensateSvcB",
      "Input": [
        "$.[businessKey]",
        "$.[user]",
        {
          "throwException": "$.[mockReduceBalanceFail]"
        }
      ],
      "Output": {
        "compensateReduceBalanceResult": "$.#root"
      },
      "Status": {
        "#root == true": "SU",
        "#root == false": "FA",
        "$Exception{java.lang.Throwable}": "UN"
      },
      "Catch": [
        {
          "Exceptions": [
            "java.lang.Throwable"
          ],
          "Next": "CompensationTrigger"
        }
      ],
      "Next": "Succeed"
    },
    "CompensateSvcA": {
      "Type": "ServiceTask",
      "ServiceName": "svcAUserService",
      "ServiceMethod": "deleteUser",
      "Input": [
        "$.[businessKey]",
        "$.[user]"
      ]
    },
    "CompensateSvcB": {
      "Type": "ServiceTask",
      "ServiceName": "svcBUserService",
      "ServiceMethod": "deleteUser",
      "Input": [
        "$.[businessKey]",
        "$.[user]"
      ]
    },
    "CompensationTrigger": {
      "Type": "CompensationTrigger",
      "Next": "Fail"
    },
    "Succeed": {
      "Type": "Succeed"
    },
    "Fail": {
      "Type": "Fail",
      "ErrorCode": "PURCHASE_FAILED",
      "Message": "purchase failed"
    }
  }
}

```


5. 应用中启动状态机

业务入口处启动编排好的状态机，调用编排好的saga长事务

```
@Autowired
StateMachineEngine stateMachineEngine;

public Object testDT(User user) {
    Map map = new HashMap();

    Map<String, Object> startParams = new HashMap<>(1);
    String businessKey = String.valueOf(System.currentTimeMillis());
    startParams.put("user", user);
    startParams.put("businessKey", businessKey);

    StateMachineInstance inst = stateMachineEngine.startWithBusinessKey("dbStateMachineApp", null, businessKey,startParams);

    log.info("saga transaction commit ****succeed****. XID: " + inst.getId());

    return map;
}

```

6. 应用DB库新建状态机脚本

```   
   CREATE TABLE seata_state_inst (
           id varchar(255) NOT NULL,
           machine_inst_id varchar(255) NOT NULL,
           name varchar(255) NOT NULL,
           type varchar(255),
           service_name varchar(255),
           service_method varchar(255),
           service_type varchar(255),
           business_key varchar(255),
           state_id_compensated_for varchar(255),
           state_id_retried_for varchar(255),
           gmt_started timestamp NOT NULL,
           is_for_update tinyint(1),
           input_params longtext,
           output_params longtext,
           status varchar(255) NOT NULL,
           excep longblob,
           gmt_updated timestamp,
           gmt_end timestamp,
           PRIMARY KEY (id)
   ) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE utf8mb4_general_ci;
   
   CREATE TABLE seata_state_machine_def (
           id varchar(255) NOT NULL,
           name varchar(255) NOT NULL,
           tenant_id varchar(255) NOT NULL,
           app_name varchar(255) NOT NULL,
           type varchar(255),
           comment_ varchar(255),
           ver varchar(255) NOT NULL,
           gmt_create timestamp NOT NULL,
           status varchar(255) NOT NULL,
           content longtext,
           recover_strategy varchar(255),
           PRIMARY KEY (id)
   ) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE utf8mb4_General_ci;
   
   CREATE TABLE seata_state_machine_inst (
           id varchar(255) NOT NULL,
           machine_id varchar(255) NOT NULL,
           tenant_id varchar(255) NOT NULL,
           parent_id varchar(255),
           gmt_started timestamp NOT NULL,
           business_key varchar(255),
           start_params longtext,
           gmt_end timestamp,
           excep longblob,
           end_params longtext,
           status varchar(255),
           compensation_status varchar(255),
           is_running tinyint(1),
           gmt_updated timestamp NOT NULL,
           PRIMARY KEY (id, machine_id),
           UNIQUE unikey_buz_tenant (business_key, tenant_id)
   ) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE utf8mb4_General_ci;
   
```