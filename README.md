# seata 使用 Saga 模式

分布式事务组件seata的使用demo，AT模式，集成nacos、springboot、springcloud、mybatis-plus，数据库采用mysql

| spring-cloud-alibaba | spring-cloud |  Boot Version |
| :--- | :--- | :---: | 
| 2.2.10-RC1 | Hoxton.SR12 | 2.3.12.RELEASE | 

## 使用记录
- 遇到TIMESTAMP类型报错时候设置:
``` 
set global explicit_defaults_for_timestamp = ON;
```
- 状态机 ServiceTask 少了参数会报主键重复

## seata-server配置nacos与db

``` 
seata:
  registry:
    # support: nacos, eureka, redis, zk, consul, etcd3, sofa
    type: nacos
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:8848
      group: SEATA_GROUP
      namespace:
      cluster: default
      username: nacos
      password: nacos
      context-path:
  store:
    # support: file 、 db 、 redis
    mode: db
    db:
      datasource: druid
      db-type: mysql
      driver-class-name: com.mysql.jdbc.Driver
      url: jdbc:mysql://127.0.0.1:3306/seata?rewriteBatchedStatements=true
      user: root
      password: admin
```

## seata-client应用配置 nacos

``` 
seata:
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:8848
      group: "SEATA_GROUP"
      namespace: ""
      username: "nacos"
      password: "nacos"
```

## Modules
### Spring-Cloud 微服务访问
- [sc-service-a:1011](sc-service-a)
- [sc-service-b:1012](sc-service-b)
- [sc-service-c:1013](sc-service-b)
- [gateway-zuul:80](gateway-zuul)

## 测试用例

测试分布式事务 svc-a -> svc-b ,svc-c
```
curl -X POST -H "Content-Type: application/json" localhost:1011/v1/user/testDT -d '{"name":"go","money":"100","id":"1"}' -v
```
money==200时候子事务 svc-b 异常，全局事务回滚
```
curl -X POST -H "Content-Type: application/json" localhost:1011/v1/user/testDT -d '{"name":"go","money":"200","id":"1"}' -v
```

money==300时候子事务 svc-c 异常，全局事务回滚
```
curl -X POST -H "Content-Type: application/json" localhost:1011/v1/user/testDT -d '{"name":"go","money":"300","id":"1"}' -v
```

基础CRUD

```
curl -X POST -H "Content-Type: application/json" localhost:1011/v1/user -d '{"name":"go","money":"100","id":"1"}' -v
curl localhost:1011/v1/user/1 -v
```

## seata自动配置功能
- io.seata.spring.boot.autoconfigure.properties.client ServiceProperties在属性加载完后根据 0 == grouplist.size() 提供默认配置
``` 
seata:
  service:
    grouplist:
      default: 127.0.0.1:8091
```

- 高可用seata-server模式下从nacos注册中心读取配置


## links
- https://blog.csdn.net/zjj2006/article/details/108723488
- https://www.sofastack.tech/blog/seata-saga-flexible-financial-applications/