package com.xiao.springcloud.canal;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * [简要描述]: Canal服务配置
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/25 15:18
 * @since JDK 1.8
 */
@Configuration
@Data
public class CanalConfig
{
    @Value("${spring.cache.server.cluster:false}")
    private boolean cluster;

    @Value("${spring.cache.server.hostName:localhost}")
    private String hostName;

    @Value("${spring.cache.server.port:11111}")
    private int port;

    @Value("${spring.cache.server.destination:example}")
    private String destination;

    @Value("${spring.cache.server.userName:}")
    private String userName;

    @Value("${spring.cache.server.password:}")
    private String password;

    @Value("${spring.cache.server.zkServers:}")
    private String zkServers;

    @Value("${spring.cache.server.dbName:}")
    private String listenerDb;
}
