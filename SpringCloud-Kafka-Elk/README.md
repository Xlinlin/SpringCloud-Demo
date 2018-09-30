1. 实现核心思路：
>> 业务日志通过logback异步输出到kafka
>> logstash 从kafka读取数据，并清洗数据(如：过滤，json格式化，删除一些字段等),输出到es<br>
>> kibana进行数据可视化查看 <br>

2.elk相关知识[参考资料](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Demo-Doc/kafka%2Belk)<br>
3.相关注意事项:<br>
>集成了kafka，所以在当kafka不可用的情况下，会有个metadata更新1分钟的时间，所以logback必须要进行异步输出<br>
>结合[@LogAnnotation](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Common/src/main/java/com/xiao/skywalking/demo/common/logaspect)注解的实现，在代码层嵌入，节省运维和管理日志文件的成本。当然也可能在一定程度上回影响业务，各自取舍<br>
