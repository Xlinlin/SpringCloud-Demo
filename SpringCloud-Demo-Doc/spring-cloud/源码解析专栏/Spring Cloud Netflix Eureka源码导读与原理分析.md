# 原文链接：https://blog.csdn.net/neosmith/article/details/53131023

Spring Cloud Netflix技术栈中，Eureka作为服务注册中心对整个微服务架构起着最核心的整合作用，因此对Eureka还是有很大的必要进行深入研究。

本文主要分为四部分，一是对项目构建的简要说明；二是对程序入口点的定位，帮助大家找到阅读源码的起点；三是对Eureka实现机制的分析；四是与使用Zookeeper相比Eureka作为注册服务的区别。

1. 源码
1.1 源码获取、构建
我们需要分别下载 Eureka 官方源码和 Spring Cloud Netflix 适配 Eureka 的代码。可以在 https://github.com/Netflix/eureka 下载到原生 Eureka 代码，在 https://github.com/spring-cloud/spring-cloud-netflix/tree/v1.2.2.RELEASE 下载Spring Cloud针对于Eureka的Spring Cloud适配。

在构建 Eureka 官方源码时一定要使用项目里自带的gradlew而不要自行下载gradle(首先要科学上网), 因为gradle早已更新到3.X版本，而Eureka用的是2.1.0版本构建的项目，新版本构建时会报错。Spring Cloud Netflix构建起来很简单，执行 mvn clean package，耐心等待即可。（我机器上是12分钟）

1.2 程序构成
Eureka: 
1. 是纯正的 servlet 应用，需构建成war包部署 
2. 使用了 Jersey 框架实现自身的 RESTful HTTP接口 
3. peer之间的同步与服务的注册全部通过 HTTP 协议实现 
4. 定时任务(发送心跳、定时清理过期服务、节点同步等)通过 JDK 自带的 Timer 实现 
5. 内存缓存使用Google的guava包实现

1.3 代码结构
模块概览： 
这里写图片描述

eureka-core 模块包含了功能的核心实现: 
1. com.netflix.eureka.cluster - 与peer节点复制(replication)相关的功能 
2. com.netflix.eureka.lease - 即”租约”, 用来控制注册信息的生命周期(添加、清除、续约) 
3. com.netflix.eureka.registry - 存储、查询服务注册信息 
4. com.netflix.eureka.resources - RESTful风格中的”R”, 即资源。相当于SpringMVC中的Controller 
5. com.netflix.eureka.transport - 发送HTTP请求的客户端，如发送心跳 
6. com.netflix.eureka.aws - 与amazon AWS服务相关的类

eureka-client模块: 
Eureka客户端，微服务通过该客户端与Eureka进行通讯，屏蔽了通讯细节

eureka-server模块: 
包含了 servlet 应用的基本配置，如 web.xml。构建成功后在该模块下会生成可部署的war包。

2. 代码入口
2.1 作为纯Servlet应用的入口
由于是Servlet应用，所以Eureka需要通过servlet的相关监听器 ServletContextListener 嵌入到 Servlet 的生命周期中。EurekaBootStrap 类实现了该接口，在servlet标准的contextInitialized()方法中完成了初始化工作：

@Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            // 读取配置信息
            initEurekaEnvironment(); 
            // 初始化Eureka Client(用来与其它节点进行同步)
            // 初始化server
            initEurekaServerContext(); 

            ServletContext sc = event.getServletContext();
            sc.setAttribute(EurekaServerContext.class.getName(), serverContext);
        } catch (Throwable e) {
            logger.error("Cannot bootstrap eureka server :", e);
            throw new RuntimeException("Cannot bootstrap eureka server :", e);
        }
    }
2.2 与Spring Cloud结合的胶水代码
Eureka是一个纯正的Servlet应用，而Spring Boot使用的是嵌入式Tomcat, 因此就需要一定的胶水代码让Eureka跑在Embedded Tomcat中。这部分工作是在 EurekaServerBootstrap 中完成的。与上面提到的EurekaBootStrap相比，它的代码几乎是直接将原生代码copy过来的，虽然它并没有继承 ServletContextListener, 但是相应的生命周期方法都还在，然后添加了@Configuration注解使之能被Spring容器感知：

这里写图片描述 
原生的 EurekaBootStrap 类实现了标准的ServletContextListener接口

这里写图片描述 
Spring Cloud的EurekaServerBootstrap类没有实现servlet接口，但是保留了接口方法的完整实现

我们可以推测，框架一定是在某处调用了这些方法，然后才是执行原生Eureka的启动逻辑。EurekaServerInitializerConfiguration类证实了我们的推测。该类实现了 ServletContextAware(拿到了tomcat的ServletContext对象)、SmartLifecycle(Spring容器初始化该bean时会调用相应生命周期方法)：

@Configuration
@CommonsLog
public class EurekaServerInitializerConfiguration
        implements ServletContextAware, SmartLifecycle, Ordered {
}
1
2
3
4
5
在 start() 方法中可以看到

eurekaServerBootstrap.contextInitialized(EurekaServerInitializerConfiguration.this.servletContext);
1
的调用，也就是说，在Spring容器初始化该组件时，Spring调用其生命周期方法start()从而触发了Eureka的启动。

@Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eurekaServerBootstrap.contextInitialized(EurekaServerInitializerConfiguration.this.servletContext); // 调用 servlet 接口方法手工触发启动
                    log.info("Started Eureka Server");

                    // ... ...
                }
                catch (Exception ex) {
                    // Help!
                    log.error("Could not initialize Eureka servlet context", ex);
                }
            }
        }).start();
    }

2.3 其它几个重要的代码入口
了解以上入口信息后，我们就可以根据自己的需要自行研读相关的代码了。这里再提示几个代码入口： 
1. com.netflix.appinfo.InstanceInfo类封装了服务注册所需的全部信息 
2. Eureka Client探测本机IP是通过org.springframework.cloud.commons.util.InetUtils工具类实现的 
3. com.netflix.eureka.resources.ApplicationResource类相当于Spring MVC中的控制器，是服务的注册、查询功能的代码入口点

3. 可能会被坑的几处原理
3.1 Eureka的几处缓存
Eureka的wiki上有一句话，大意是一个服务启动后最长可能需要2分钟时间才能被其它服务感知到，但是文档并没有解释为什么会有这2分钟。其实这是由三处缓存 + 一处延迟造成的。

首先，Eureka对HTTP响应做了缓存。在Eureka的”控制器”类ApplicationResource的109行可以看到有一行

String payLoad = responseCache.get(cacheKey);
1
的调用，该代码所在的getApplication()方法的功能是响应客户端查询某个服务信息的HTTP请求：

String payLoad = responseCache.get(cacheKey); // 从cache中拿响应数据

if (payLoad != null) {
       logger.debug("Found: {}", appName);
       return Response.ok(payLoad).build();
} else {
       logger.debug("Not Found: {}", appName);
       return Response.status(Status.NOT_FOUND).build();
}

上面的代码中，responseCache引用的是ResponseCache类型，该类型是一个接口，其get()方法首先会去缓存中查询数据，如果没有则生成数据返回（即真正去查询注册列表），且缓存的有效时间为30s。也就是说，客户端拿到Eureka的响应并不一定是即时的，大部分时候只是缓存信息。

其次，Eureka Client对已经获取到的注册信息也做了30s缓存。即服务通过eureka客户端第一次查询到可用服务地址后会将结果缓存，下次再调用时就不会真正向Eureka发起HTTP请求了。

**再次， 负载均衡组件Ribbon也有30s缓存。**Ribbon会从上面提到的Eureka Client获取服务列表，然后将结果缓存30s。

最后，如果你并不是在Spring Cloud环境下使用这些组件(Eureka, Ribbon)，你的服务启动后并不会马上向Eureka注册，而是需要等到第一次发送心跳请求时才会注册。心跳请求的发送间隔也是30s。（Spring Cloud对此做了修改，服务启动后会马上注册）

以上这四个30秒正是官方wiki上写服务注册最长需要2分钟的原因。

3.2 服务注册信息不会被二次传播
如果Eureka A的peer指向了B, B的peer指向了C，那么当服务向A注册时，B中会有该服务的注册信息，但是C中没有。也就是说，如果你希望只要向一台Eureka注册其它所有实例都能得到注册信息，那么就必须把其它所有节点都配置到当前Eureka的peer属性中。这一逻辑是在PeerAwareInstanceRegistryImpl#replicateToPeers()方法中实现的：

private void replicateToPeers(Action action, String appName, String id,
                                  InstanceInfo info /* optional */,
                                  InstanceStatus newStatus /* optional */, boolean isReplication) {
        Stopwatch tracer = action.getTimer().start();
        try {
            if (isReplication) {
                numberOfReplicationsLastMin.increment();
            }
            // 如果这条注册信息是其它Eureka同步过的则不会再继续传播给自己的peer节点
            if (peerEurekaNodes == Collections.EMPTY_LIST || isReplication) {
                return;
            }

            for (final PeerEurekaNode node : peerEurekaNodes.getPeerEurekaNodes()) {
                // 不要向自己发同步请求
                if (peerEurekaNodes.isThisMyUrl(node.getServiceUrl())) {
                    continue;
                }
                replicateInstanceActionsToPeers(action, appName, id, info, newStatus, node);
            }
        } finally {
            tracer.stop();
        }
    }

3.3 多网卡环境下的IP选择问题
如果服务部署的机器上安装了多块网卡，它们分别对应IP地址A, B, C，此时： 
Eureka会选择IP合法(标准ipv4地址)、索引值最小(eth0, eth1中eth0优先)且不在忽略列表中(可在application.properites中配置忽略哪些网卡)的网卡地址作为服务IP。 
这个坑的详细分析见：http://blog.csdn.net/neosmith/article/details/53126924

4. 作为服务注册中心，Eureka比Zookeeper好在哪里
著名的CAP理论指出，一个分布式系统不可能同时满足C(一致性)、A(可用性)和P(分区容错性)。由于分区容错性在是分布式系统中必须要保证的，因此我们只能在A和C之间进行权衡。在此Zookeeper保证的是CP, 而Eureka则是AP。

4.1 Zookeeper保证CP
当向注册中心查询服务列表时，我们可以容忍注册中心返回的是几分钟以前的注册信息，但不能接受服务直接down掉不可用。也就是说，服务注册功能对可用性的要求要高于一致性。但是zk会出现这样一种情况，当master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举。问题在于，选举leader的时间太长，30 ~ 120s, 且选举期间整个zk集群都是不可用的，这就导致在选举期间注册服务瘫痪。在云部署的环境下，因网络问题使得zk集群失去master节点是较大概率会发生的事，虽然服务能够最终恢复，但是漫长的选举时间导致的注册长期不可用是不能容忍的。

4.2 Eureka保证AP
Eureka看明白了这一点，因此在设计时就优先保证可用性。Eureka各个节点都是平等的，几个节点挂掉不会影响正常节点的工作，剩余的节点依然可以提供注册和查询服务。而Eureka的客户端在向某个Eureka注册或时如果发现连接失败，则会自动切换至其它节点，只要有一台Eureka还在，就能保证注册服务可用(保证可用性)，只不过查到的信息可能不是最新的(不保证强一致性)。除此之外，Eureka还有一种自我保护机制，如果在15分钟内超过85%的节点都没有正常的心跳，那么Eureka就认为客户端与注册中心出现了网络故障，此时会出现以下几种情况： 
1. Eureka不再从注册列表中移除因为长时间没收到心跳而应该过期的服务 
2. Eureka仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点上(即保证当前节点依然可用) 
3. 当网络稳定时，当前实例新的注册信息会被同步到其它节点中

因此， Eureka可以很好的应对因网络故障导致部分节点失去联系的情况，而不会像zookeeper那样使整个注册服务瘫痪。

5. 总结
Eureka作为单纯的服务注册中心来说要比zookeeper更加“专业”，因为注册服务更重要的是可用性，我们可以接受短期内达不到一致性的状况。不过Eureka目前1.X版本的实现是基于servlet的java web应用，它的极限性能肯定会受到影响。期待正在开发之中的2.X版本能够从servlet中独立出来成为单独可部署执行的服务。
