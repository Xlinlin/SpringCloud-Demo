**springcloud config client改造**

1. 在客户端发起rest请求获取配置时，在http header中添加当前提供服务的端口号，参考：<br>
``com.xiao.custom.config.client.configuration.ConfigServicePropertySourceLocator.getSecureRestTemplate``

2. 新增refresh包，提供refresh API接口，技术关键点：<br>
``com.xiao.custom.config.client.refresh.component.RefreshBeanConfig``类<br>
``@ComponentScan``注解，用于扫描提供的api接口bean对象注入到spring容器中<br>

3. ``@Configuration 和 @EnableConfigurationProperties`` 注解，来确定唯一引用位置 resouces/META-INF/spring.factories文件(SPI机制)<br>
该文件内记录了需要加载的@Configuration注解类：<br>
    ```$xslt
    # Auto Configure
    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    com.xiao.custom.config.client.configuration.ConfigClientAutoConfiguration
    
    # Bootstrap components
    org.springframework.cloud.bootstrap.BootstrapConfiguration=\
    com.xiao.custom.config.client.configuration.ConfigServiceBootstrapConfiguration,\
    com.xiao.custom.config.client.configuration.DiscoveryClientConfigServiceBootstrapConfiguration
    ```
4. 引入netty，实现心跳机制监听服务端客户端连接状态。<br>
       A. 支持自定义netty端口，服务端和客户端自定义填写，配置参数：netty.server.port，默认使用8999<br>
       B. springboot的 server.port 一定要配置到 bootstrap.yml配置文件中，客户端需要使用到该端口号上报到服务端<br>
       C. springboot + netty 简单设计实现：<br>
          心跳维持在线状态<br>
          初始化连接时，模拟一个登陆操作，以此绑定netty连接端口和服务端所提供的端口，用于断开下线使用<br>
          客户端失去心跳或异常连接，服务端监听到，标记应用离线<br>
       D. netty实现配置刷新方案---待完善<br>