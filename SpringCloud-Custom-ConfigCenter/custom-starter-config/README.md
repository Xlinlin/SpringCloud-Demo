1. 客户端引用该pom，使用springcloud config client组件。
2. spring.provides文件指定了具体的模块：
    ```$xslt
    provides: custom-config-client
    ```
3. POM关键配置
    ```$xslt
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter</artifactId>
    </dependency>
     <dependency>
        <groupId>com.xiao.skywalking.demo</groupId>
        <artifactId>custom-config-client</artifactId>
    </dependency>
    ```