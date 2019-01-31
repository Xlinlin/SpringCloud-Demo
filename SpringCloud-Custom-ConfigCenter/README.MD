**Spring-cloud config注册中心扩展**

_1._ **使用自定义读取配置源，基于mysql的配置读取实现**。<br>

_2._ **设计简述**：<br>
 1) 基于IP、应用名称、LABEL、Profile获取配置 <br>
 1.1) IP主要用于区分不同区域获取不同配置信息 <br>
 1.2) 应用名称+Label+profile遵循原spring-cloud config的设计 <br>
 
 2) 表结构设计，参考：configMysql.sql文件 <br>
 2.1) 维护应用与区域之间的关系，即在获取配置时，依据请求IP查询所属区域对应的配置信息。<br>
 2.2) 抽象出具体配置项、配置组、应用配置信息。即配置组可挂多个配置项，应用环境配置挂多个配置组，以达到资源共享。<br>
 2.3) 支持单个服务私有配置<br>
 2.4) 维护客户端连接信息，提供服务的IP和端口<br>
 
 3) 整体设计<br>
 3.1) center-server为服务端，单独部署启动，也可以集成到自己的应用，直接使用``@CustomEnableConfigServer``注解加上spring jdbc的配置即可：
```
spring:
  application:
    name: winner-config-server
  #mysql
  datasource:
    url: jdbc:mysql://192.168.206.210:3306/config_center?useSSL=false
    username: admin
    password: Admin@123
    driver-class-name: com.mysql.jdbc.Driver
    # 使用druid数据源
    type: com.alibaba.druid.pool.DruidDataSource
```
 3.2) center-service为Rest服务端，提供配置、应用、区域等维护的API，可单独部署应用也可以集成到web工程中 <br>
 3.3) center-web 提供前端的页面管理 <br>
 3.4) center-client 客户端jar包 <br>
 3.5) starter-config 客户端引用该工程即可 <br>
 3.6) 客户端查询配置核心时序图，参考：配置中心查询核心时序图.png <br>
 3.7) 客户端刷新实现简介：<br>
    A. 改造原springcloud client，在发起restTemplate的时候，将客户端服务的端口上报到配置服务端，并添加refresh刷新的接口，其实际是调用了ContextRefresher.refresh()方法<br>
    B. 配置服务端保存应用+环境+应用服务的IP：PORT信息到数据库<br>
    C. 配置管理界面修改应用配置，进行发布配置刷新客户端配置<br>
    D. 客户端引用starter-config jar包，并在需要刷新的配置类上添加@RefreshScope注解。[custom-config-simple](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Custom-ConfigCenter/custom-config-simple)使用方法<br>
    E. 服务端发起刷新配置应用，通过应用+环境查找该应用下提供的所有应用服务，调用refresh刷新客户端的配置，如果调用失败标记该服务下线，下一次不进行刷新操作<br>
    F. springcloud的刷新参考资料：<br>
        [数据源刷新参考](https://www.jianshu.com/p/5acd4db7cd5e)<br>
        [Spring Cloud Config 是如何实现热更新的](http://www.scienjus.com/spring-cloud-refresh/)<br>
        [配置刷新基本流程](https://blog.csdn.net/cml_blog/article/details/78411312)<br>
        
_3._**配置中心高可用方案理论**<br>
既然使用spring-cloud套件，可结合eureka注册中心来实现高可用。其他详细设计可参考携程开源的apollo的高可用设计
