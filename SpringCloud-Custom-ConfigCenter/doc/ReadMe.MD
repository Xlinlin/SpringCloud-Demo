**Spring-cloud config注册中心扩展**

_1._ 使用自定义读取配置源，基于mysql的配置读取实现。<br>
_2._ 设计简述：<br>
 1) 基于IP、应用名称、LABEL、Profile获取配置 <br>
 1.1) IP主要用于区分不同区域获取不同配置信息 <br>
 1.2) 应用名称+Label+profile遵循原spring-cloud config的设计 <br>
 2) 表结构设计，参考：MysqlTable.sql文件 <br>
 2.1) 维护应用与区域之间的关系，即在获取配置时，依据请求IP查询所属区域对应的配置信息。<br>
 2.2) 抽象出具体配置项、配置组、应用配置信息。即配置组可挂多个配置项，应用环境配置挂多个配置组，以达到资源共享。<br>
 3) 整体设计<br>
 3.1) 采用SpringCloud实现，Center-Server为服务端，单独部署启动，也可以集成到自己的应用，直接使用``@CustomEnableConfigServer``注解加上spring jdbc的配置即可：
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
 3.2) Center-Web 提供前端的页面管理 <br>
 3.3) Center-Demo 为提供使用的demo <br>
 3.4) 客户端查询配置核心时序图，参考：配置中心查询核心时序图.png
 
_3._配置中心高可用方案理论<br>
既然使用spring-cloud套件，可结合eureka注册中心来实现高可用。其他详细设计可参考携程开源的apollo的高可用设计 



