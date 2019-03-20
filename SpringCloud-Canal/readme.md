1. 阿里开源数据同步工具[Canal](https://github.com/alibaba/canal)，简单DEMO实用记录。<br>
2. Canal：可解析Mysql的 binlog日志，来实现增量数据缓存同步、ES同步。亦或者做自定义业务处理 <br>
3. 官方有案例结合消息队里实用，以及如何实现高可用<br>
4. [Canal服务端搭建](https://www.jianshu.com/p/6299048fad66)
4. Java client 操作 [参考JAVA]()<br>
DEMO输出结果：
```$xslt
Db name:数据库名
table name: 表名
UPSERT id=14,具体的数据字段以及值，自定义处理
```
