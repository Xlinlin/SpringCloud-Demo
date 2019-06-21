1. 在需要记录日志的方法上添加 ``@LogAnnotation`` 注解即可
2. 实现原理：<br>
   > 主要利用annotation注解+aop方式 <br>
   > 具体实现见：``LogAspect``类 <br>
3. 日志记录采用``LogService`` 接口，当前底层实现为slf4j，可自定义扩展
4. 结合ekl+kafka案例,[参考](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Kafka-Elk)