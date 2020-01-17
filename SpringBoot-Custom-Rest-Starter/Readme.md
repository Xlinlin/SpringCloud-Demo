Springboot Rest Template配置

1. 支持Ok Http和Http连接池模式，内嵌包装成HttClientService服务，并提供完成的请求日志处理
2. 引入pom:
    ```$xslt
    <dependency>
        <groupId>com.purcotton.omni</groupId>
        <artifactId>omni-common-rest-starter</artifactId>
        <version>${project.version}</version>
    </dependency>
    ```

3. Ok Http使用：
    ```$xslt
        rest:
          # okhttp 配置
          okhttp:
            enable: true
            connection-timeout: 12000
            read-timeout: 30000
            write-timeout: 12000
    ```
   
4. Http pool使用：
    ```$xslt
        rest
          # http pool
          pool:
            enable: true
            max-total: 20
            default-max-per-route: 2
            validate-after-inactivity: 2000
            connect-timeout: 10000
            connection-request-timeout: 10000
            socket-timeout: 10000
    ```
5. 同步异步使用：
    ```$xslt
       rest:
         http:
           service:
             sync: true
             async: false
    ```
   
6. 使用方式，推荐使用HttpClientService，因为提供了完整的日志记录：
    ```$xslt
            // 使用包装http client
           @Autowired
           private HttpClientService httpClientService;
           
           // 使用 resttemplate
           @Autowired
           private RestTemplate restTemplate;
    ```
7. HttpClientService日志处理，实现HttpRequestLogService接口
    ```$xslt
    public class HttpLogServiceImpl implements HttpRequestLogService
        {
            /**
             * [简要描述]:保存日志信息<br/>
             * [详细描述]:<br/>
             *
             * @param requestLog :
             * llxiao  2019/4/24 - 14:42
             **/
            @Override
            public void saveRequestLog(HttpRequestLog requestLog)
            {
                // 日志输出
                log.info("Example log : {}", JSONObject.toJSONString(requestLog));
            }
        }
    ```