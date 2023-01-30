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

### [mesh-service-b](mesh-service-b)
- IDE本地访问 mesh-service-b:远程调用spring-cloud-gateway/sc-service-c
``` 
curl -v http://localhost:1015/api/sc/b
```
- 通过istio-gateway访问 mesh-service-b
``` 
curl -v http://172.16.20.185:30061/api/sc/b
```
- 通过spring-cloud sc-service-a 访问
``` 
curl -v localhost:1011/api/sc/a
```
- mesh-service-b中访问 spring-cloud-gateway
```
curl -v http://172.16.20.172/sc-service-c/api/sc/c 
```

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