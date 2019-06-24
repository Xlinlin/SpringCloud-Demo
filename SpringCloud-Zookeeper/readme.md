Zookeeper一些学习记录：<br>
1. **zookeeper HA 实现主备切换**，[原博文地址](http://blog.sina.com.cn/s/blog_1312c919b0102v1a9.html)<br>
   主要实现思路：<br>
   ![avatar](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Zookeeper/img/HA.png)
   >1.1 启动server时注册一个临时的有序的子节点（注意，一定要是临时有序的），将自己注册的子节点保存在一个全局变量中<br>
   
   >1.2 获取父节点下所有的子节点，排序，然后将自己的节点与最小子节点比较，如果相等则成为主机，不相等则等待。<br>
   
   >1.3 实现Watcher接口，当父节点发生变化时，执行上两个步骤<br>
2. **zookeeper HA 实现负载均衡**，[原博文地址](http://blog.sina.com.cn/s/blog_1312c919b0102v1aa.html)<br>
   主要实现思路：<br>
   >2.1 注册：首先你需要确定一个父节点，在这里父节点的名称暂且就叫/parentNode；<br>
   每个server端启动时首先向zk的集群的父节点/parentNode下去注册一个临时的子节点，<br>
   这样当有N台server时，注册的子节点就是/parentNode/server1、/parentNode/server2.........、/parentNode/serverN。<br>
   节点的数据就存每台server的ip 与port，这样你服务端不管是用rmi协议还是http协议，都可以向这个服务器发送请求了。<br>
   
   >2.2 获取服务列表：在Client端实现轮询分发的功能，实现Watcher接口，这会让你实时的监控服务端的变化。<br>
   首先去获取父节点/parentNode下所有的子节点，得到之前存的ip与port，然后将这些列表缓存到一map中，<br>
   这里就叫serverUrlCacheMap。由于实现了Watcher接口，当父节点发生变化时zk 的集群会通知Client端，<br>
   此时Client端只要重新获取父节点下所有子节点的数据，重新缓存即可<br>
   
   >2.3 轮询分发：定义一个全局变量index ，每次发起请求时，直接去serverUrlCacheMap中获取这个编号的URL，<br>
   然后发送给server，就可达到轮询分发的功能。<br>
3. **zookeeper 的EPHEMERAL节点机制实现服务集群的陷阱**，[原博文地址](https://yq.aliyun.com/articles/227260)<br>