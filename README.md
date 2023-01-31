# gs-spring-cloud

验证 spring-cloud 与 istio 相互调用 

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

## links
https://cloud.spring.io/spring-cloud-static/Finchley.SR4/single/spring-cloud.html

## zipkin下载
https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.23.2/zipkin-server-2.23.2-exec.jar