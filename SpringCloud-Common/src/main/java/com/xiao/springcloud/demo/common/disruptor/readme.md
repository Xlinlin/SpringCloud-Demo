1. Disruptor: 开源的并发框架,能够在无锁的情况下实现网络的Queue并发操作,其他更多[详情介绍](http://ifeve.com/disruptor/)
2. 本common包封装的``Disruptor``与``Spring的Event``事件组合，实现业务在JVM内解耦。<br>
3. 引入disruptor pom依赖:<br>
    ```
       <disruptor.version>3.4.2</disruptor.version>
       <!-- disruptor -->
        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
            <version>${disruptor.version}</version>
        </dependency>
    ```
4. 启动disruptor:<br>
    ```html
    // spring容器初始化时，启动disruptor
    @Component
    public class ConfigInit implements InitializingBean
    {
        @Autowired
        private DisruptorProducer disruptorProducer;
    
        @Override
        public void afterPropertiesSet()
        {
            disruptorProducer.doStart();
        }
    }
    ```
5. 生产者调用:<br>
    ``DisruptorProducer.send(BasisData data)``方法 <br>
    ```html
     // 数据数据定义
     public ServiceData extends BasisData{
        // field
        // setter and getter
     }
     
     // 注入DisruptorProducer
     @Autowired
     private DisruptorProducer disruptorProducer;
     
     @Test
     public void testSend(){
          ServiceData data = new ServiceData();
          // 设置事件类型，可预定义在EventEnum枚举类中，一个String类型
          data.setEvent(EventEnum.LOG_EVENT.getEvent());
          // 发送数据
          disruptorProducer.send(data);
     }
 
    ```
6. 业务消费者：<br>
    实现``org.springframework.context.ApplicationListener<ServiceEvent>``的``onApplicationEvent(ServiceEvent serviceEvent)``方法<br>
    ```html
     // event消费者实现
     @Component
     public class LogEventEvent implements ApplicationListener<ServiceEvent>{
         
         // 可以使用spring的异步实现@Async注解，需要配合启动类中添加 @EnableAsync注解 开启异步的支持
         @Async
         @Override
         public void onApplicationEvent(ServiceEvent event)
         {
             // 这个event事件名称要跟发送的时候事件名称一样的
             if (EventEnum.LOG_EVENT.getEvent().equals(event.getEvent()))
             {
                 ServiceData serviceData = (ServiceData) event.getSource();
                 // 进一步业务逻辑处理 todo
             }
         }
     }
    ```
