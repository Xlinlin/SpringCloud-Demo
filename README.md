# spring-cloud-demo
## 不定期更新与记录在springcloud开发中所遇到的坑以及解决方法


1. spring-cloud and skywalking demo
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
[代码实现参考](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/skywalking/demo/common/cache)<br>
[junit测试参考](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-SearchService/src/test/java/com/xiao/springcloud/test/cache)<br>

20181016
1. redis缓存 redisson客户端添加批处理

20181018
1. [spring-cache+guava 添加本地缓存](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/skywalking/demo/common/cache)

20181022
1. springboot 2.0 改造。
2. 最大的影响就是euraka和feign以及zipkin，zipkin服务端结合mq暂未调试
3. 另外guava结合的内存缓存也有所变更。
