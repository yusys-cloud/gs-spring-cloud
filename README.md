# gs-spring-cloud

分布式事务组件seata的使用demo，AT模式，集成nacos、springboot、springcloud、mybatis-plus，数据库采用mysql

| spring-cloud-alibaba | spring-cloud |  Boot Version |
| :--- | :--- | :---: | 
| 2.2.10-RC1 | Hoxton.SR12 | 2.3.12.RELEASE | 

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
https://cloud.spring.io/spring-cloud-static/Finchley.SR4/single/spring-cloud.html

## zipkin下载
https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.23.2/zipkin-server-2.23.2-exec.jar