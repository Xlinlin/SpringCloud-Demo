1. redisson yml配置加载，支持单机、集群、云托管、sentinel模式<br>
2. 配置文件中添加配置文件即可开启redisson的配置：<br>
`` redisson.fileName: redission-cluster(自定义)``
3. 提供缓存基本服务和分布式服务: <br>
``> CacheService 提供缓存基础服务`` <br>
``> DistributedService 提供分布式**可重入公平/非公平锁**、**读写锁**、**闭锁**``<br>

20181018  添加本地缓存
1. 引入spring cache和guava jar包
```
       <dependency> 
            <groupId>com.google.guava</groupId> 
            <artifactId>guava</artifactId> 
            <version>25.1-jre</version> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework.boot</groupId> 
            <artifactId>spring-boot-starter-cache</artifactId> 
        </dependency> 
```
2. 代码方法上使用
```
@Cacheable(value = "methodName", key = "'methodName'.concat({#param1}).concat({#parma2})")
```
