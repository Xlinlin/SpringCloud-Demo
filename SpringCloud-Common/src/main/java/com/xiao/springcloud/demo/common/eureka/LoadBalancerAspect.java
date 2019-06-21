package com.xiao.springcloud.demo.common.eureka;

import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerStats;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.stereotype.Component;

/**
 * [简要描述]: 快速标记服务down
 * [详细描述]:
 * Eureka注册中心的设计思想基于满足CAP分布式理论中的AP，某一个service宕机，Eureka server有自我保护机制，不会实时踢掉service，<br>
 * 默认会等到3个心跳周期也就是90秒，注册中心才会标记该service下线，所以该service的调用方才能被感知到。<br>
 * 以下方式可以解决客户端在调用某个服务的provider时实时踢掉该服务，并通知Eureka Server，<br>
 * 避免LB机制在Hystirx断路器起作用前尽量避免调用已经宕机的服务提供方<br>
 * [参考](https://www.jianshu.com/p/f75d66f6d2cc)<br>
 *
 * @author llxiao
 * @version 1.0, 2018/12/10 15:48
 * @since JDK 1.8
 */
@Slf4j
@Aspect
@Component
public class LoadBalancerAspect
{
    @Autowired
    private SpringClientFactory springClientFactory;

    @Around(value = "execution (* org.springframework.cloud.client.loadbalancer.LoadBalancerClient.reconstructURI(..)))")
    public Object reconstructURIAround(final ProceedingJoinPoint joinPoint) throws Throwable
    {

        Object[] objects = joinPoint.getArgs();
        ServiceInstance instance = (ServiceInstance) objects[0];
        Server server = new Server(instance.getHost(), instance.getPort());

        RibbonLoadBalancerContext context = springClientFactory.getLoadBalancerContext(instance.getServiceId());
        ServerStats serverStats = context.getServerStats(server);

        Object obj = joinPoint.proceed();

        if (log.isDebugEnabled())
        {
            log.debug("=======================================================================");
            log.debug(serverStats.toString());
            log.debug("=======================================================================");
        }

        /**
         * 连续网络链接失败2次以上，迅速标记该provider下线
         */
        int n = serverStats.getSuccessiveConnectionFailureCount();
        if (n > 1)
        {
            if (log.isDebugEnabled())
            {
                log.debug("===================================================================");
                log.debug("Mark server:{}-{} to down!!!", server.getHost(), server.getPort());
                log.debug("===================================================================");
            }
            context.getLoadBalancer().markServerDown(server);
        }
        return obj;
    }

}
