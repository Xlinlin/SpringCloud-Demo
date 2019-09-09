package com.xiao.custom.elasticsearch.start.autoconfig.properties;

import lombok.Data;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * [简要描述]: Elasticsearch 配置类
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/28 10:18
 * @since JDK 1.8
 */
@Data
@ConfigurationProperties(prefix = ElasticsearchProperties.ELASTIC_SEARCH_PREFIX)
public class ElasticsearchProperties
{
    public static final String ELASTIC_SEARCH_PREFIX = "spring.elasticsearch.rest";

    /**
     * 集群名称
     */
    private String clusterName;

    /**
     * 节点信息
     */
    @NestedConfigurationProperty
    private List<HostInfo> hosts;

    /**
     * 鉴权使用
     */
    private String username;
    private String password;

    /**
     * 高亮前缀
     */
    private String highlightPre = "";
    /**
     * 高亮后缀
     */
    private String highlightPost = "";

    /**
     * 连接超时时间
     */
    private int connectTimeout = RestClientBuilder.DEFAULT_CONNECT_TIMEOUT_MILLIS;

    /**
     * socket超时时间
     */
    private int socketTimeout = RestClientBuilder.DEFAULT_SOCKET_TIMEOUT_MILLIS;

    /**
     * 请求超时时间
     */
    private int requestTimeout = RestClientBuilder.DEFAULT_CONNECT_TIMEOUT_MILLIS;

    /**
     * 最大连接数
     */
    private int maxConnect = RestClientBuilder.DEFAULT_MAX_CONN_TOTAL;
    /**
     * 单主机并发最大数
     */
    private int maxConnectRoute = RestClientBuilder.DEFAULT_MAX_CONN_PER_ROUTE;

    /**
     * 重试最大超时时间
     */
    private int maxRetryTimeout = RestClientBuilder.DEFAULT_MAX_RETRY_TIMEOUT_MILLIS;

}
