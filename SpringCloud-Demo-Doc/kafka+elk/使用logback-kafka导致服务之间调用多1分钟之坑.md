1. 背景： 
 `使用logback+kafka+elk 进行日志监控`
2. 问题：
  `服务之间调用，始终发现有多1分钟的时间消耗，如：A调用B服务，需要消耗1分钟才返回给A`
3. 原因：
  `最终发现的是，logback写入kafka日志，而kafka的配置是单机且是个不可用的服务问题所导致`
4. 分析：
  `debug跟踪源码，发现即使kafka的服务配置是错误的，也依旧能创建成功cluster和producer，最终在producer进行send的时候触发
  Metadata的更新，二者之间的version不一致，需要无限循环更新并调用Object.wait(timeout)方法阻塞直到超时`
  超时源码部分：org.apache.kafka.clients.Metadata.awaitUpdate
  `` 
           long remainingWaitMs = maxWaitMs;
           while (this.version <= lastVersion) {
               if (remainingWaitMs != 0)
                   wait(remainingWaitMs);
               long elapsed = System.currentTimeMillis() - begin;
               if (elapsed >= maxWaitMs)
                   throw new TimeoutException("Failed to update metadata after " + maxWaitMs + " ms.");
               remainingWaitMs = maxWaitMs - elapsed;
           }
   ``
   remainingWaitMs的超时间取的是默认的max.block.ms时间 60000ms
5. 建议解决方法： 
   `5.1: 使用logbach的异步输出，防止kafka不可用时阻塞主线程日志(本案暂时采用)`
   `5.2: 缩短wait的超时时间，通过配置metadata.fetch.timeout.ms`
   `5.3: 保证kafka的高可用`
6. 疑惑：
   `其实很不明白，为啥我明明给的是一个不可用的broker地址，为啥还可以创建cluster和producer成功，猜测应该是保证高可用`
7. elk+kafka配置参考：SpringCloud-Kafka-Elk项目配置
   