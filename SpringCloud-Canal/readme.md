1. 本DEMO主要实现：Canal+disruptor+springboot，订阅mysql binlog日志，实现数据同步，比如缓存，ES等。<br>

2. 组件简介：<br>
    [Canal](https://github.com/alibaba/canal)-阿里巴巴mysql数据库binlog的增量订阅&消费组件<br>
    [Disruptor](https://github.com/LMAX-Exchange/disruptor)-开源的并发框架，能够在无锁的情况下实现网络的Queue并发操作<br>

3. 使用方式：<br>
    Canal的服务端搭建[参考](https://www.jianshu.com/p/6299048fad66)<br>
    Disruptor+Canal异步操作[参考](https://github.com/Xlinlin/SpringCloud-Demo/tree/master/SpringCloud-Canal/src/main/java/com/xiao/springcloud)<br>
    业务集成时在[DisruptorServiceImpl](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Canal/src/main/java/com/xiao/springcloud/disruptor/service/impl/DisruptorServiceImpl.java)<br>
    服务中实现自己的业务逻辑即可，代码片段：<br>
   ```$xslt
    public void execute(TableData tableData)
    {
        if (log.isDebugEnabled())
        {
            log.debug("接受数据更新请求，更新表名:{},更新的主键为:{}", tableData.getTableName(), tableData.getId());
        }
        if (null != tableData)
        {
            //业务处理 TODO
        }
    }
   ```
   在[CanalClientService](https://github.com/Xlinlin/SpringCloud-Demo/blob/master/SpringCloud-Canal/src/main/java/com/xiao/springcloud/canal/CanalClientService.java)中定制自己需要的binlog事件处理<br>
   代码片段：
   ```$xslt
     private void processData(List<CanalEntry.Entry> entrys)
        {
            if (log.isDebugEnabled())
            {
                log.debug("接收到需要处理数据，总数量：{}", entrys.size());
            }
    
            List<TableData> tableDataList = new ArrayList<>(entrys.size());
            // 表名
            String tableName;
            TableData tableData;
            CanalEntry.RowChange rowChange;
            for (CanalEntry.Entry entry : entrys)
            {
                // 事物数据不处理
                if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN
                        || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND)
                {
                    continue;
                }
    
                if (entry.getEntryType() != CanalEntry.EntryType.ROWDATA)
                {
                    continue;
                }
    
                //获取表名
                tableName = entry.getHeader().getTableName();
    
                //仅处理部分表接口数据
                if (tableNames.contains(tableName))
                {
                    try
                    {
                        rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                        // RowData --具体insert/update/delete的变更数据，可为多条，1个binlog event事件可对应多条变更，比如批处理
                        for (CanalEntry.RowData rowData : rowChange.getRowDatasList())
                        {
                            switch (rowChange.getEventType())
                            {
                                //当前仅处理Insert和更新数据
                                case INSERT:
                                case UPDATE:
                                    tableData = new TableData();
                                    tableData.setTableName(tableName);
                                    tableData.setId(parseKey(rowData.getAfterColumnsList()));
                                    tableDataList.add(tableData);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        log.error("当前行数据解析错误：", e);
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(tableDataList))
            {
                //发送数据到异步队列框架
                disruptorProducer.send(tableDataList);
            }
            if (log.isDebugEnabled())
            {
                log.debug("接收到Mysql更新数据，总数量：{}", tableDataList.size());
            }
        }
   ```

4. Canal使用——数据库初始化配置（5.* 必须）<br>
    _tips: mysql 8.* 默认开启以下配置_ <br>
    在[mysqld]下配置：<br>
    ```$xslt
    log-bin=mysql-bin 
    binlog-format=ROW 
    server_id=1(任意id即可，但不能与canal中指定的id相同)
   ```
