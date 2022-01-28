# gs-spring-cloud

验证 spring-cloud 与 istio 相互调用 

| Release Train |  Boot Version |
| :--- | :---: | 
| Finchley.SR4 | 2.0.9.RELEASE | 

## Modules
### Spring-Cloud 微服务访问
- [sc-service-a:1011](sc-service-a)
- [sc-service-b:1012](sc-service-b)
- [sc-service-c:1013](sc-service-b)
- [gateway-zuul:80](gateway-zuul)
### 改造 sc-service-b 为 ServiceMesh 架构
- [mesh-sc-sidecar-service-b:1014](mesh-sc-sidecar-service-b) 迁移过程中 proxy 服务，将spring-cloud 架构流量转发到 ServiceMesh 架构中
- [mesh-service-b:1015](mesh-service-b) ServiceMesh 架构下改造的 sc-service-b,部署到 istio 中

## 测试用例
- sc-service-a 访问 sc-service-b
``` 
curl -v localhost:1011/api/sc/a
```
- sc-service-b停止，sc-service-b-sidecar 代理流量到 ServiceMesh 集群内
- 通过zuul网关访问
```
curl -i localhost/sc-service-a/api/sc/a
curl -i localhost/sc-service-c/api/sc/c
```

## Build
sc-service-b 升级为 mesh-service-b 后，部署到 istio 集群中
```
cd mesh-service-b
mvn clean package
docker build -t='yusyscloud/mesh-service-b' .
docker push yusyscloud/mesh-service-b 
```


## POC env
 - 启动sidecar java -jar polyglot-sidecar-1.0-SNAPSHOT.jar --eureka.client.serviceUrl.defaultZone=http://192.168.251.173:8761/eureka
 - 启动sidecar同主机的go应用 ./go-web-linux
 - 启动SC微服务 java -jar gs-consumer-1.0-SNAPSHOT.jar --eureka.client.serviceUrl.defaultZone=http://192.168.251.173:8761/eureka
 - 启动zipkin
 
## POC tests 
 - SC微服务访问 -> sidecar   curl localhost:3002/test/go
 - SC微服务被访问 <- sidecar  curl localhost:3000/test/java
 
 sleuth与spriing-cloud-sidecar功能
 - sidecar支持sleuth链路追踪
 - sidecar默认集成eureka服务注册发现
 - @EnableSidecar包含了@EnableCircuitBreaker, @EnableDiscoveryClient, @EnableZuulProxy
 - 异构系统通过zuul访问其他微服务,其他微服务通过feignclient直接访问到异构系统的IP+端口(sidecar将异构系统端口定义到了eureka-client中)

## links
https://cloud.spring.io/spring-cloud-static/Finchley.SR4/single/spring-cloud.html

## zipkin下载
https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.23.2/zipkin-server-2.23.2-exec.jar