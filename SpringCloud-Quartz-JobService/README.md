**SpringCloud + Quartz 实现任务调度，可配置，可通过API操作**

1. 实现思路:<br>
   主要是通过``SchedulerFactoryBean``将Quartz 集成到spring容器中,然后开放API，注册到eureka上<br>
   [参考文章1](https://blog.csdn.net/pengjunlee/article/details/78965877)<br>
   [参考文章2](https://blog.csdn.net/beliefer/article/details/51578546)<br>
2. TaskSchedulerFactory 用于接入quartz到spring容器中<br>
   JobConfig 初始化SchedulerFactoryBean和Scheduler<br>
   JobManager 动态添加、删除、停止、恢复定时任务 <br>
   ServiceTaskExecuteJob 主要业务逻辑实现，可在该实现类中调用定时调用远程方法 <br>
   