1. logstash配置
```
input {
    kafka {
        #kafka集群
        bootstrap_servers => ["ip1:port1,ip2:port2,ip3:port3"]
        # 5个线程消费
        consumer_threads => 5
        # 主题
        topics => ["omniLogstash"]
        group_id => "omniLogstash"
        client_id => "omniLogstash"
        # 每次从最后位置开始读取
        auto_offset_reset => "latest"
        decorate_events => true
        type => "bhy"
    }
}
filter {
    # 日志中不包含该条件的，不进行处理，直接删除（过滤一些不必要的日志）
    if ([message] =~  "^(?!.*?clsName).*$") {
        drop {}
    }
    # 第一层解析，需要保留message
    json {
      source => "message"
      #remove_field => ["message"]
    }
    # 第二次嵌套解析，解析后删除message
    json {
      source => "message"
      remove_field => ["message"]
    }
    # 删除多余字段
    mutate {
        remove_field => "kafkaServer"
        remove_field => "logger"
        remove_field => "kafkaLogTopic"
        remove_field => "level"
        remove_field => "@version"
        remove_field => "X-Span-Export"
        remove_field => "X-B3-SpanId"
        remove_field => "X-B3-ParentSpanId"
        remove_field => "X-B3-TraceId"
        remove_field => "levelVal"
    }
}
output {
    # 输出到es
    elasticsearch {
        hosts => ["192.168.206.203:9200"]
        index => "logstash-kafka-%{+YYYY-MM-dd}"
    }
    # 控制台打印调试
    stdout { codec => rubydebug }
}
```
2. 日志格式对比<br>
 解析前:<br>
```
{
          "type" => "bhy",
      "@version" => "1",
       "message" => "{\"@timestamp\":\"2018-09-27T10:41:55.575+08:00\",\"@version\":1,\"message\":\"{\\\"clientHost\\\":\\\"172.16.80.194\\\",\\\"clientIp\\\":\\\"172.16.80.194\\\",\\\"clientPort\\\":56722,\\\"clsName\\\":\\\"com.purcotton.omni.content.management.commdity.api.rest.MpcCommodityRestService\\\",\\\"costTime\\\":141,\\\"errorCode\\\":0,\\\"methodName\\\":\\\"queryMpCommodityDetail\\\",\\\"params\\\":[\\\"10000\\\",\\\"10000\\\"],\\\"requestTime\\\":1538016115418,\\\"requestUri\\\":\\\"/commodity/mp/detail\\\",\\\"responseTime\\\":1538016115562,\\\"result\\\":{\\\"brandId\\\":1,\\\"brandName\\\":\\\"\\u5168\\u68C9\\u65F6\\u4EE31\\\",\\\"brandNo\\\":\\\"10000\\\",\\\"categoryId\\\":12,\\\"categoryName\\\":\\\"\\u53E3\\u7EA2\\\",\\\"categoryNo\\\":\\\"100010202\\\",\\\"commodityNo\\\":\\\"10000\\\",\\\"commodityTitle\\\":\\\"\\u53E4\\u9F99\\u9999\\u6C34300ml\\\",\\\"freight\\\":0,\\\"goodsDtos\\\":[{\\\"goodsNo\\\":\\\"1000010\\\",\\\"specification\\\":\\\"500ml\\\"},{\\\"goodsNo\\\":\\\"1000011\\\",\\\"specification\\\":\\\"300ml\\\"}],\\\"goodsNo\\\":\\\"1000011\\\",\\\"haitao\\\":0,\\\"labels\\\":[{\\\"id\\\":1,\\\"labelColor\\\":\\\"#C0FF3E\\\",\\\"labelName\\\":\\\"\\u62A4\\u80A4\\\",\\\"labelNo\\\":\\\"100\\\",\\\"status\\\":1},{\\\"id\\\":2,\\\"labelColor\\\":\\\"#AB82FF\\\",\\\"labelName\\\":\\\"\\u4FDD\\u6E7F\\\",\\\"labelNo\\\":\\\"101\\\",\\\"status\\\":1},{\\\"id\\\":3,\\\"labelColor\\\":\\\"#8FBC8F\\\",\\\"labelName\\\":\\\"\\u5973\\u58EB\\\",\\\"labelNo\\\":\\\"102\\\",\\\"status\\\":1}],\\\"orgPrice\\\":88.05,\\\"salPrice\\\":59.99,\\\"salesPoint\\\":\\\"\\u70ED\\u5356\\u5546\\u54C1\\\",\\\"shopCode\\\":\\\"10000\\\",\\\"specification\\\":\\\"300ml\\\",\\\"taxation\\\":0.00,\\\"zPicUrl\\\":[\\\"http://omni-test.oss-cn-shenzhen.aliyuncs.com//omni/commdity/picture/commdity/SP5484/JL10001_z_01.jpg\\\",\\\"http://omni-test.oss-cn-shenzhen.aliyuncs.com//omni/commdity/picture/commdity/SP5484/JL10001_z_01.jpg\\\"]},\\\"serverHost\\\":\\\"1000-LLXIAO\\\",\\\"serverIp\\\":\\\"172.16.80.194\\\",\\\"serverPort\\\":7778,\\\"status\\\":0}\",\"logger\":\"com.purcotton.omni.common.aop.log.Slf4jLogService\",\"thread\":\"http-nio-7778-exec-1\",\"level\":\"INFO\",\"levelVal\":20000,\"springAppName\":\"content-management-service\",\"kafkaServer\":\"192.168.206.201:9092,192.168.206.202:9092,192.168.206.203:9092\",\"kafkaLogTopic\":\"omniLogstash\",\"X-Span-Export\":\"false\",\"X-B3-SpanId\":\"3997d1db9d4067cd\",\"X-B3-TraceId\":\"3997d1db9d4067cd\"}",
    "@timestamp" => 2018-09-27T02:41:55.737Z
}

```
 解析后：<br>
```
{
      "X-B3-SpanId" => "e28a86370c36562b",
       "requestUri" => "/commodity/mp/detail",
       "serverHost" => "1000-LLXIAO",
         "serverIp" => "172.16.80.194",
           "logger" => "com.purcotton.omni.common.aop.log.Slf4jLogService",
            "level" => "INFO",
          "clsName" => "com.purcotton.omni.content.management.commdity.api.rest.MpcCommodityRestService",
     "responseTime" => 1538016255932,
       "serverPort" => 7778,
     "X-B3-TraceId" => "e28a86370c36562b",
       "clientHost" => "172.16.80.194",
      "kafkaServer" => "192.168.206.201:9092,192.168.206.202:9092,192.168.206.203:9092",
      "requestTime" => 1538016255856,
    "kafkaLogTopic" => "omniLogstash",
           "status" => 0,
        "errorCode" => 0,
       "methodName" => "queryMpCommodityDetail",
       "@timestamp" => 2018-09-27T02:44:15.933Z,
           "thread" => "http-nio-7778-exec-6",
         "clientIp" => "172.16.80.194",
         "@version" => 1,
             "type" => "bhy",
       "clientPort" => 57050,
    "X-Span-Export" => "false",
           "params" => [
        [0] "10000",
        [1] "10000"
    ],
    "springAppName" => "content-management-service",
         "levelVal" => 20000,
         "costTime" => 76,
           "result" => {
             "brandName" => "全棉时代1",
               "goodsNo" => "1000011",
        "commodityTitle" => "古龙香水300ml",
                "haitao" => 0,
            "salesPoint" => "热卖商品",
               "brandId" => 1,
           "commodityNo" => "10000",
              "orgPrice" => 88.05,
          "categoryName" => "口红",
              "shopCode" => "10000",
         "specification" => "300ml",
               "brandNo" => "10000",
            "categoryNo" => "100010202",
            "categoryId" => 12,
               "freight" => 0,
              "salPrice" => 59.99
    }
}
```

3. 遇到的坑：<br>
太相信ToStringBuilder的 Json格式，导致多次配置json都解析失败，最终使用fast.JSON做日志打印<br>
更多配置参考[common](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Common)和[elk](https://github.com/Xlinlin/spring-cloud-demo/tree/master/SpringCloud-Kafka-Elk)工程代码
