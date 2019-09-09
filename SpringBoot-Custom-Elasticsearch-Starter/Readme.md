**定制SpringBoot Starter 之Elasticsearch Rest High Level Client Starter**

**1. 自定义SpringBoot Starter 三要素：**
>1.1.pom :
```$xslt
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>

        <!-- 将被@ConfigurationProperties注解的类的属性注入到元数据 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```
>1.2. 注解使用
```$xslt
@Data
@ConfigurationProperties(prefix = ElasticsearchProperties.ELASTIC_SEARCH_PREFIX)
public class ElasticsearchProperties{}

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchAutoConfiguration{}
```
>1.3 EnableAutoConfiguration 利用SpringFactoriesLoader机制加载所有的AutoConfiguration类 META-INF/spring.factories
```$xslt
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.xiao.custom.elasticsearch.start.autoconfig.ElasticsearchAutoConfiguration
```

Elasticsearch高级客户端打包集成为Springboot Starter包，详情参考[Example工程](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringBoot-Custom-Elasticsearch-Starter/Custom-Elasticsearch-Starter-Example)<p>
**2. Custom ElasticSearch High Level Rest Client Starter使用说明：**<p>
>2.1Pom引入<br>
```$xslt
     
       <properties>
               <elasticsearch.version>6.3.2</elasticsearch.version>
       </properties>

       <dependencies>
        <dependency>
            <groupId>com.purcotton.omni</groupId>
            <artifactId>omni-common-elasticsearch-starter</artifactId>
            <version>${project.version}</version>
        </dependency>

        <dependency>
            <groupId>org.elasticsearch</groupId>
            <artifactId>elasticsearch</artifactId>
            <version>${elasticsearch.version}</version>
        </dependency>

        <dependency>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-client</artifactId>
            <version>${elasticsearch.version}</version>
        </dependency>

        <dependency>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-high-level-client</artifactId>
            <version>${elasticsearch.version}</version>
        </dependency>
       </dependencies>
```
>2.2配置文件：<br>
```$xslt
spring:
  elasticsearch:
    rest:
      clusterName: omni-dev-es
      hosts:
        -
          hostname: 192.168.206.210
          port: 9200
          schema: http
        #-
          #hostname: 192.168.206.212
          #port: 9200
          #schema: http
      #username: you username
      #password: you passwd
      # 连接超时时间,单位ms,默认1S
      connectTimeout: 1000
      # socket超时时间,单位ms,默认30S
      socketTimeout: 30000
      # 请求超时时间,单位ms,默认500ms
      requestTimeout: 500
      # 单机最大连接数,默认30个
      maxConnect: 30
      # 单机最大并发数,默认10个
      maxConnectRoute: 10
      # 最大重试时间,默认30S
      maxRetryTimeout: 30000

```
>2.3代码引用：<br>
```$xslt
    @Autowired
    private RestHighLevelClient restHighLevelClient;
```
>2.4ElasticSearch High Level Rest Client 增删改Demo：<br>
[ElasticsearchApplicationTest](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringBoot-Custom-Elasticsearch-Starter/Custom-Elasticsearch-Starter-Example/src/test/java/com/xiao/custom/elasticsearch/starter/example/ElasticsearchApplicationTest.java)<br>
```$xslt
   // 创建索引
   ElasticsearchApplicationTest.testCreateIndex()
   // 索引是否存在
   ElasticsearchApplicationTest.testIndexExist()
   // 删除索引
   ElasticsearchApplicationTest.testDelIndex()
   // 打开和关闭索引
   ElasticsearchApplicationTest.testOpenAndCloseIndex()
   // 添加文档
   ElasticsearchApplicationTest.testInsert()
   // 主键ID获取文档
   ElasticsearchApplicationTest.testGetIndex()
   // 更新文档
   ElasticsearchApplicationTest.testUpdate()
   // 搜索
   ElasticsearchApplicationTest.testQuery()
   // 删除文档
   ElasticsearchApplicationTest.testDel()
   // 批处理1
   ElasticsearchApplicationTest.testBulkRequest()
   // 批处理2
   ElasticsearchApplicationTest.testBulkProcessor()
```