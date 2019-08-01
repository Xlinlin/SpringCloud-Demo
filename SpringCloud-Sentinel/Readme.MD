**SpringBoot + SpringCloud + Feign + Sentinel 集成实现接口限流监控**

SpringBoot版本：1.5.13.RELEASE <br>
SpringCloud版本：Edgware.SR4 <br>
Sentinel[详情介绍](https://github.com/alibaba/Sentinel)版本：0.1.2.RELEASE 是集成了[spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba)<br>

[注册中心-eureka](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Sentinel/SpringCloud-Sentinel-Eureka) <br>
[服务提供-producer](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Sentinel/SpringCloud-Sentinel-Producer) <br>
[服务消费-consumer](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Sentinel/SpringCloud-Sentinel-Consumer) <br>
[Sentinel Dashbord](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Sentinel/dashbord)<br>

Demo使用说明：
1. Dashboard启动：
java -Dserver.port=8080 -jar sentinel-dashboard.jar
[官网参考](https://github.com/alibaba/Sentinel/wiki/%E6%8E%A7%E5%88%B6%E5%8F%B0)
2. 启动erueka --> 启动Producer --> 启动Consumer
3. 展示图片:
![Producer](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Sentinel/img/sentinel-dashbord-producer.png)
![Consumer](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Sentinel/img/sentinel-dashbord-consumer.png)

简要配置说明：
1. dashboard启动后，客户端接入：<br>
    ```html
     properties配置：
     # Sentinel dashbord 数据交互端口，注意单机多服务部署端口冲突
     spring.cloud.sentinel.transport.port=8729
     # sentinel  dashbord地址
     spring.cloud.sentinel.transport.dashboard=192.168.206.212:8880
     
     pom文件引入(自动引入相关依赖包)：
     <dependency>
         <groupId>org.springframework.cloud</groupId>
         <artifactId>spring-cloud-alibaba-sentinel</artifactId>
         <version>0.1.2.RELEASE</version>
     </dependency>
  
    ```
2. producer和consumer使用sentinel仅引入 上一步中的**pom**文件即可
3. feign的支持：
   ```html
    pom引入：
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
        <version>1.4.7.RELEASE</version>
    </dependency>
 
    开启sentinel支持
    # 使用阿里 sentinel监控
    feign.sentinel.enabled=true
 
   ```

重要提示：
1. Sentinel版本要与springboot和springcloud大版本一致，否则会出现不可预见的异常
2. 接入到dashbord后，要请求下url，然后稍等等个1分钟左右才能到控制台，不要着急


