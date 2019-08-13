**SpringCloud Feign Hystrix 熔断、线程使用坑记录**

坑：<br>
1. 并发时Feign提供的服务其中一个接口出现超时异常次数多了，导致该Feign整个服务不可用，其实是hystrix进行了熔断处理，[Hystrix参考资料](https://segmentfault.com/a/1190000012439580)
2. Feign提供服务，并发起来10个以上，就会出现线程池拒绝异常``RejectedExecutionException``，也是Hystrix搞的鬼，其默认线程池是10个，[关于Hystrix线程池的参考资料](https://www.cnblogs.com/seifon/p/9921774.html)

参考文章中说明了几点会出现熔断(打开断路器)的：一个是请求到达一定的阈值、一个是错误发生超过了一定的比例。<br>

**第一个问题：**<br>
是由于feign的超时出现了错误，当并发访问达到一定错误比例的是，hystrix变开启了断路器(服务级别)，那么该服务下所有的接口将变得不可用。<br>
首先我们看hystrix的超时通用配置：<br>
```$xslt
hystrix:
  command:
    ## 默认全局配置
    default:
      #fallback:
        # 是否关闭回退方法
        #enable: true
        #isolation:
          #semaphore:
            ## 当触发fallback时，并发最大出发fallback的数量，超过该数量直接拒绝服务
            #maxConcurrentRequests: 1000
      execution:
        # 断路器
        circuitBreaker:
          #确定断路器是否用于跟踪运行状况和断路请求
          #enable: true
          #当在配置时间窗口内达到此数量的失败后，进行短路。默认20个，如：10s内请求失败数量达到20个，断路器开
          requestVolumeThreshold: 20
          #短路多久以后开始尝试是否恢复，默认5s,缺省情况采用的是连续错误次数来隔离，一旦一个实例访问成功，马上恢复实例的访问
          sleepWindowInMilliseconds: 5000
          #出错百分比阈值，当达到此阈值后，开始短路。默认50%,(建议业务不要使用)
          #errorThresholdPercentage: 50
          #强制打开熔断器，如果打开这个开关，那么拒绝所有request，默认false
          #forceOpen: false
          #强制关闭熔断器 如果这个开关打开circuit将一直关闭且忽略circuitBreaker.errorThresholdPercentage
          #forceClosed: false
          #关闭超时熔断功能
        #timeout:
         #enable: false
        isolation:
          # 隔离策略：有THREAD(默认)(单独的线程上执行，并发请求受线程池中的线程数量的限制)和SEMAPHORE(调用线程上执行，并发请求受到信号量计数的限制)
          #strategy: THREAD
          #semaphore:
            #当触发fallback时，并发最大出发fallback的数量，超过该数量直接拒绝服务，默认10，SEMAPHORE模式有效
            #maxConcurrentRequests: 10
          thread:
            #设置熔断超时时间，默认1S，容易出现 fallback available 异常
            #在THREAD模式下，达到超时时间，可以中断，在SEMAPHORE模式下，会等待执行完成后，再去判断是否超时
            timeoutInMilliseconds: 5000
            #调用线程允许请求HystrixCommand.GetFallback()的最大数量，默认10
            #maxConcurrentRequests: 10
            #在发生超时时是否应中断,默认值：true,THREAD模式有效
            #interruptOnTimeout: true
            #当发生取消时，执行是否应该中断，默认值：true,THREAD模式有效
            #interruptOnCancel: true
```
这里主要关注：``hystrix.command.execution.circuitBreaker.isolation.thread.timeoutInMilliseconds``这一项配置，默认值是2S，
并且这个配置的值要小于``ribbon.ReadTimeout``配置时间。<br>
其次，既然是单个接口导致的服务超时，其他接口是可用的，而这里又是全局的配置，所以Hystrix也提供了针对接口级别的配置：<br>
```$xslt
hystrix:
 command:
   ## 接口级别配置，其格式：feign名称#方法名(参数类型)
   #ProducerFeign#timeout(String):
     #execution:
       #isolation:
         #thread:
           #timeoutInMilliseconds: 5000
```
此外，hystrix使用[Archaius](https://github.com/Netflix/archaius)来提供动态属性修改的支持，扩展步骤：<br>
>> 1.初始化配置获取源，这里可以从配置中心、redis、zookeeper等，实现``com.netflix.config.PolledConfigurationSource``接口的poll方法，[参考类：DynamicConfigSource](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Hystrix-Demo/SpringCloud-Hystrix-Consumer/src/main/java/com/xiao/hystrix/demo/consumer/dynamic/DynamicConfigSource.java)<br>
>> 2.配置并初始化自动配置``com.netflix.config.DynamicConfiguration``，通过定时任务去定时刷新的，[参考类：InitHystrixConfiguration](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Hystrix-Demo/SpringCloud-Hystrix-Consumer/src/main/java/com/xiao/hystrix/demo/consumer/dynamic/InitHystrixConfiguration.java)<br>
>> 3.其他更多参考博文资料：[hystrix 简单使用以及动态配置更新](https://my.oschina.net/u/1169457/blog/1787414/)、[zookeeper的动态配置](https://github.com/tigerMoon/demo-hystrix-config)<br>

以上能实现接口级别的隔离配置，来解决部分处理耗时的接口超时到时服务不可用的问题。<br>

**第二个问题：**<br>
并发起来，超过个10个线程出现线程拒绝异常``java.util.concurrent.RejectedExecutionException``<br>
同样套路，我们来先看下配置:<br>
```$xslt
hystrix:
  # 线程池
  threadpool:
    default:
      #默认为10,基本得原则时保持线程池尽可能小，他主要是为了释放压力，防止资源被阻塞
      coreSize: 50
      ## 最大排队长度。默认-1,不能动态调整
      maxQueueSize: 1000
      #动态控制线程池队列的上限，即使maxQueueSize没有达到，达到queueSizeRejectionThreshold该值后，请求也会被拒绝，默认值5
      #queueSizeRejectionThreshold: 800
```
默认线程池的配置只有10，而maxQueueSize也没有配置，queueSizeRejectionThreshold默认为5，那么超过10个线程并发请求即会出现线程拒绝异常了。<br>
这里建议是：``coreSize``尽可能的小，``maxQueueSize``是不可动态修改的，``queueSizeRejectionThreshold``参数可以动态调整，可以用到上述第一个问题动态修改的套路使用，
但这个值超过maxQueueSize的值就会失效<br>
其他参考博文：[踩坑 Spring Cloud Hystrix 线程池队列配置](https://www.cnblogs.com/seifon/p/9921774.html)

**此外，这里再扩展两个优化点**：<br>
1. Hystrix请求缓存，请注意，仅针对同一线程内请求的缓存。这里使用注解来实现，步骤：<br>
>> 1.实现一个filter,用来拦截所有请求的时候，初始化hystrix的上下文，[参考类:HystrixCacheFilter](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Hystrix-Demo/SpringCloud-Hystrix-Consumer/src/main/java/com/xiao/hystrix/demo/consumer/filter)<br>
>> 2.需要一个@ServletComponentScan注解来扫描到这个filter，[参考类：HystrixCacheConfiguration](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Hystrix-Demo/SpringCloud-Hystrix-Consumer/src/main/java/com/xiao/hystrix/demo/consumer/config/HystrixCacheConfiguration.java)<br>
>> 3.接口上使用@CacheResult注解，[参考类:ProducerFeign](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Hystrix-Demo/SpringCloud-Hystrix-Consumer/src/main/java/com/xiao/hystrix/demo/consumer/feign/ProducerFeign.java)<br>

2. 针对Feign服务接口做一层JVM缓存，提高短时间内重复请求的吞吐量和效率，本案例使用：springboot cache + caffeine 实现，接口上使用@Cacheable注解标记，[参考类:CaffeineCacheConfiguration](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Hystrix-Demo/SpringCloud-Hystrix-Consumer/src/main/java/com/xiao/hystrix/demo/consumer/config/CaffeineCacheConfiguration.java)<br>
 
[Github-Demo地址](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Hystrix-Demo)
