**SpringCloud Sleuth Stream Zipkin Kafka Elasticsearch 实现简单链路跟踪**  

_注意版本号zipkin使用的是2.4.2，SpringCloud版本Dalston.SR5_
1. **服务端主要配置**<br>
**pom配置:**:
```$xslt
<!-- zipkin + kafka +es -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-sleuth-zipkin</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-sleuth</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-sleuth-zipkin-stream</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-stream-binder-kafka</artifactId>
        </dependency>

        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-autoconfigure-ui</artifactId>
            <version>${zipkin.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.zipkin.java</groupId>
            <artifactId>zipkin-autoconfigure-storage-elasticsearch-http</artifactId>
            <version>${zipkin.version}</version>
        </dependency>
```
**配置文件 application.properties:**
```$xslt
#采样率，推荐0.1，百分之百收集的话存储可能扛不住
spring.sleuth.sampler.percentage=1
spring.sleuth.enabled=false
maxHttpHeaderSize=8192

### kafka链接和zk的链接
spring.cloud.stream.kafka.binder.brokers=192.168.206.203:9092
spring.cloud.stream.kafka.binder.zkNodes=192.168.206.203:2181 

## 使用es做存储
zipkin.storage.StorageComponent=elasticsearch
zipkin.storage.type=elasticsearch
zipkin.storage.elasticsearch.hosts=192.168.206.204:9200
#es集群名称
zipkin.storage.elasticsearch.cluster=zipkin-es
zipkin.storage.elasticsearch.index=zipkin-db
zipkin.storage.elasticsearch.index-shards=5
zipkin.storage.elasticsearch.index-replicas=1
```
**启动注解:**
```$xslt
@EnableZipkinStreamServer
```
代码参考[SpringCloud-ZipkinServer](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-ZipkinServer)

2.**客户端配置**<br>
**pom配置:**
```$xslt
 <!-- zipkin服务 改造 sleuth stream + zipkin  + es + kafka -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-sleuth</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-stream-binder-kafka</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-sleuth-stream</artifactId>
        </dependency>
```
**参数配置application.properties:**
```$xslt
### spring 配置
spring:
  ##  zipkin 链路跟踪配置
  sleuth:
    enabled: true
    #采样率，越高会有性能影响
    sampler:
      percentage: 1.0
  cloud:
    ## kafka zk配置 配合zipkin
    stream:
      kafka:
        binder:
          brokers: 192.168.206.203:9092
          zkNodes: 192.168.206.203:2181
```
代码参考[SpringCloud-Provider](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Provider)和[SpringCloud-Consumer](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Consumer)<br>


[更多参考资料](https://www.jianshu.com/p/d2a71e242ca8)