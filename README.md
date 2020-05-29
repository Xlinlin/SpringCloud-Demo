# spring-cloud-demo
## 不定期更新与记录在springcloud开发中所遇到的坑以及解决方法

初始化添加
1. spring-cloud skywalking demo
2. add mqtt suports
3. add 微信小程序 suports mqtt
4. kafka elk支持


20180809
1. 更新 代码结构
2. 新增git配置中心
3. 新增kafka elk demo配置以及文档
4. 文档结构整理
5. 分离注册中心和配置中心
7. 添加zipkin服务跟踪


20180905
1. 自定义注解实现aop日志
2. 自定义注解实现实体类参数校验
3. 添加mybatis自定生成映射实体类、mapper等
4. 添加全局异常处理
5. 添加fegin自定义数据解析

20180907
1. 添加注解，作为参数校验入口

20180910
1. 解决服务之间调用fegin+hystrix 熔断异常拦截处理

20180914
1. 服务调用之间的rest请求，参数为对象时需要添加@RequestBody注解
``eg:
saveRegionCity(@RequestBody RegionCityDto regionCityDto)``
2. 服务间调用接口的返回值，不能使接口返回，必须要使用实现类返回，fegin客户端获取不到数据返回Null
`eg:
 public User getUser(@RequestBody UserQuery query); 
 User必须为实现类，不能为接口`
3. 添加fastjson解析，解决部分调用对象内包含对象传值为空问题

20180921
1. Doc 目录结构调整
2. 记录[elk+kafka+logback服务之间调用多1分钟时间之坑](https://github.com/Xlinlin/spring-cloud-demo/blob/master/SpringCloud-Demo-Doc/kafka%2Belk/使用logback-kafka导致服务之间调用多1分钟之坑.md)

20180927
1. [elk+logstash+logback解析嵌套json数据](https://github.com/Xlinlin/spring-cloud-demo/blob/master/SpringCloud-Demo-Doc/kafka%2Belk/ELK%E6%97%A5%E5%BF%97logstash%E8%A7%A3%E6%9E%90JSON%E5%B5%8C%E5%A5%97.md)

20180930
1. 添加guava+spring-cache本地缓存实现，[参考入口](https://blog.csdn.net/mafei6827/article/details/80868931)
2. 记录springcloud 1.x版本解决feignclient下requestmapping与springmvc的定义冲突问题，[参考入口](http://blog.didispace.com/spring-cloud-feignclient-problem/?utm_source=tuicool&utm_medium=referral)

20181009
1. 添加ES实现电商[搜索基础服务](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-SearchService)
2. 添加Mockito实现api的junit测试

20181012
1. redisson yml配置加载，支持单机、集群、云托管、sentinel模式<br>
2. 配置文件中添加配置文件即可开启redisson的配置：<br>
`` redisson.fileName: redission-cluster(自定义)``
3. 提供缓存基本服务和分布式服务: <br>
``> CacheService 提供缓存基础服务`` <br>
``> DistributedService 提供分布式**可重入公平/非公平锁**、**读写锁**、**闭锁**``<br>
[代码实现参考](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/cache)<br>
[junit测试参考](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-SearchService/src/test/java/com/xiao/springcloud/test/cache)<br>

20181016
1. redis缓存 redisson客户端添加批处理

20181018
1. [spring-cache+guava 添加本地缓存](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/cache)

20181022
1. 升级Springboot2.0  详情参考springboot2.0分之
2. 调整common包，可打成jar包
3. 添加启动shell脚本，参考common包script目录下.sh文件

20181027
1. bootstrap.sh 脚本参数简化

20181029
1. 优化bootstrap.sh脚本
2. 添加jenkins构建后自动部署脚本
3. 添加jenkins构建后远程自动部署脚本
[详情](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Common/script)

20181114
1. Sharding-sphere尝试

20181115
1. bootstrap.sh 脚本添加jvm参数配置，以及停止时旧日志文件的备份
2. 本地和远程自动部署时，不进行原服务包的删除，按时间戳进行备份原来的可执行包

20181127
1. springcloud-config 自定义mysql实现，[详情](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Custom-ConfigCenter)


20181210
1. 工程结果整理
2. 添加springboot+quartz自定义实现 任务调度

20190119
1. 新增Redisson集成springdata，使用RedisTemplate,[详情](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Redisson)

20190124
1. 新增RedisTemplate 使用pipeline批量操作redis数据
2. 添加常用工具类 AES加解密、MD5等

20190126
1. 改造zipkin链路跟踪实现：SpringCloud Sleuth Stream Zipkin Kafka Elasticsearch 实现简单链路跟踪。<br>
[参考](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-ZipkinServer/README.md)

20190131
1. 自定义配置中心重构，[详情](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Custom-ConfigCenter)
2. 新增多条件搜索测试，[详情](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-SearchService/src/test/java/com/xiao/springcloud/test/SearchManagerTest.java)<br>
3. 更多多条件搜索的[参考资料](http://www.scienjus.com/elasticsearch-function-score-query/)

20190201
1. 新增[Docker + SpringBoot + Maven 构建发布到远程仓库 DEMO](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Docker)

20190320
1. 新增 ES 权重查询 以及 聚合逻辑，[详情参考](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-SearchService/src/main/java/com/xiao/spring/cloud/search/es/service/SearchServiceEsImpl.java)
2. 新增阿里开源数据同步工具[Canal](https://github.com/alibaba/canal)的简单[Demo使用](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Canal)

20190327
1. 将Canal+Disruptor整合到springboot中，提供一套完整的Canal异步框架，在DisruptorServiceImpl服务中实现自己的业务逻辑即可，[更多详见](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Canal)

20190402
1. 自定义配置中心，引入Netty监测心跳[详情](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Custom-ConfigCenter)

20190405
1. netty实现配置刷新[详情](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Custom-ConfigCenter)

20190411
1. 添加maven+jenkins+docker+springboot 构建打包发布部署的jenkins shell脚本

20190402
1. 修复linux系统，客户端异常断开，服务端无感知问题，即在linux上使用kill或ctrl+c 中断服务，无法进入exceptionCaught方法导致无法感知应用下线问题。<br>
更换为channelInactive方法来感知和下线客户端(netty)

20190504
1. [新增mqtt通过nginx代理配置](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-MQTT)

20190515
1. 新增spring session+ spring security +  jwt简单鉴权，[参考入口](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Custom-ConfigCenter/custom-config-web)

20190611
1. 记录 fork join demo[详情](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/forkjoin)

20190621
1. Disruptor+spring event封装[详情以及使用说明](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/disruptor)

20190624
1. [入手Zookeeper](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Zookeeper)

20190702
1. 新增Canal启动 [ServerRunningMonitor部分源码注解记录](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Canal/doc/ServerRunningMonitor%E6%BA%90%E7%A0%81%E6%B3%A8%E8%A7%A3.md)，
2. 修复定时任务重新启动时加载启动状态且已过期的任务报错问题：[新增在添加任务是校验表达的合法性](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Quartz-JobService/src/main/java/com/xiao/springcloud/job/util/CronExpUtil.java)

20190720
1. Springboot-Admin 2.0服务端+Springboot-Admin 1.5.6客户端[集成使用](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringBoot-Admin)，掌控你的微服务。

20190801
1. [SpringBoot + SpringCloud + Feign + Sentinel 集成实现接口限流监控](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Sentinel)

20190813
1. [SpringCloud + Feign + Hystrix 熔断、线程池的一些坑记录](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Hystrix-Demo)

20190909 
1. [定制SpringBoot Starter 之Elasticsearch Rest High Level Client Starter](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringBoot-Custom-Elasticsearch-Starter)

20190910
1. [启动脚本添加GC参数和skywalking探针](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Common/script/bootstrap.sh)

20190929
1. 新增图片比较工具类，比较两张图片是否相同：[DHashUtil](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/util/image/ImageDHashUtil.java)&[PHashUtil](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/util/image/ImagePHashUtil.java)

20191019
1. redisson分布式锁库存使用，下单、取消单、出库单之jmeter ifelse程序并发测试--**预告**

20191022
1. [Springboot官方文档-配置新-Tomcat优化](https://docs.spring.io/spring-boot/docs/2.2.1.BUILD-SNAPSHOT/reference/html/appendix-application-properties.html#server-properties)

20191023
1. [Jmeter+Springboot+Redisson分布式锁并发订单操作(下单、取消单、完成单、加库存)](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringBoot-Stock-Demo)

20191105
1. Api对外接口统一返回值，如：{"code":200,"erroMsg":"",data:{}}，[参考实现](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Consumer/src/main/java/com/xiao/skywalking/consumer/common/advice/UnifiedReturnAdvice.java)

20191119
1. Springboot 的Rest请求返回的Response中的HTTP响应行只有：HTTP/1.1 200 {OK},无OK返回导致老的http客户端无法识别，是因为springboot 1.4以上版本将tomcat升级到了8.5.x以后的版本，如果需要支持，需要设置tomcat的版本低于8.5的版本，设置：
    ```$xslt
    <properties>
      <tomcat.version>8.0.29</tomcat.version>
    </properties> 
    ```
    [参考资料1](http://www.mamicode.com/info-detail-2280850.html);<br>
    [参考资料2](https://stackoverflow.com/questions/49610522/spring-boot-return-http-1-1-200-not-http-1-1-200-ok);
    
20191125
1. [Gitlab+P3C-PMD(ali)标准化你团队的代码.doc](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90/Gitlab%2BP3C-PMD(Aliyun)%E6%A0%87%E5%87%86%E5%8C%96%E4%BD%A0%E5%9B%A2%E9%98%9F%E7%9A%84%E4%BB%A3%E7%A0%81.docx)

20191206
1. [SpringCloud RestTemplate 封装stater](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Custom-RestTemplate-Stater)支持使用http连接池、okhttp等

20191208
1. [Linux之netstat命令](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/linux/Linux-netstat%E5%91%BD%E4%BB%A4.md)-服务自动化发布时以此结果为依据停止服务

20191213
1. [RxJava Hello World](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/rxjava/RxJavaHelloWorld.MD)，要入手一定要敲代码，敲起来!

20200104
1. Shell脚本+jstat+curl+crontab 监控JVM发短信[脚本](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90/monitor-jstat.sh)

20200111
1. [基于Springboot1.5.9+SpringCloud+Zipkin+ELK链路跟踪实现](https://github.com/Xlinlin/spingcloud-zipkin-elk-demo)

20200114
1. String字符GBK和UTF编码格式长度判断以及截取

20200115
1. 经常用到对list进行分页批处理，写了[工具类](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/util/ListPageUtil.java)一劳永逸

20200117
1. 封装RestTemplate,支持okhttp,httpool,支持同步和异步请求,[ReadMe.MD](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringBoot-Custom-Rest-Starter)

20200224
1. Springboot web应用签名包括工具类,[传送链接](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/springcloud/demo/common/sign)

20200229
1. K8S Cluster[安装文档](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/k8s/K8S%20Linux%20%20Centos%207%E5%AE%89%E8%A3%85.docx)

20200422
1. Sonarqube+Gitlab-CICD构建[代码质量管理平台](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90)

20200426
1. 完善代码质量[监控体系](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/%E6%8C%81%E7%BB%AD%E9%9B%86%E6%88%90/%E4%BB%A3%E7%A0%81%E8%B4%A8%E9%87%8F%E7%9B%91%E6%8E%A7%E4%BD%93%E7%B3%BB%E6%96%B9%E6%A1%88.pptx)

20200508
1. SpringCloud Gateway + nacos实现灰度， + ribbon实现全链路版本请求，[详情](https://github.com/Xlinlin/SpringCloud-Gateway-Canary)

20200527
1. [添加docker-build脚本，执行脚本构建镜像并推送到私服仓库](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Docker/docker-build.sh)

20200528
1. swagger2 创建[api文档](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/swagger%E7%94%9F%E6%88%90html%E6%96%87%E4%BB%B6.pdf)整理
2. [docker-swarm集群监控文档](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Demo-Doc/docker)整理

20200529
1. 补充swarm集群部署springcloud项目，[详细文档](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Demo-Doc/docker/docker-swarm-springcloud.md)