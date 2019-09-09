package com.xiao.custom.elasticsearch.start.autoconfig;

import com.alibaba.fastjson.JSONObject;
import com.xiao.custom.elasticsearch.start.autoconfig.properties.ElasticsearchProperties;
import com.xiao.custom.elasticsearch.start.autoconfig.properties.HostInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * [简要描述]: 自动装配
 * [详细描述]: <p>
 * EnableAutoConfiguration 利用SpringFactoriesLoader机制加载所有的AutoConfiguration类 META-INF/spring.factories
 *
 * @author llxiao
 * @version 1.0, 2019/8/28 10:28
 * @since JDK 1.8
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
@Slf4j
public class ElasticsearchAutoConfiguration implements DisposableBean
{
    private RestHighLevelClient restHighLevelClient;

    @Bean
    @ConditionalOnMissingBean
    public RestHighLevelClient restHighLevelClient(ElasticsearchProperties elasticsearchProperties)
    {
        if (log.isDebugEnabled())
        {
            log.debug("初始化Elasticsearch Rest High Level Client....");
        }
        List<HostInfo> hosts = elasticsearchProperties.getHosts();
        if (CollectionUtils.isEmpty(hosts))
        {
            throw new RuntimeException("Elasticsearch host配置为空，请检查：spring.elasticsearch.rest.hosts的配置是否正确");
        }

        if (log.isDebugEnabled())
        {
            log.debug("Elasticsearch host: {}", JSONObject.toJSONString(hosts));
        }

        HttpHost[] httpHosts = new HttpHost[hosts.size()];
        int i = 0;
        for (HostInfo host : hosts)
        {
            httpHosts[i++] = new HttpHost(host.getHostname(), host.getPort(), host.getSchema());
        }

        RestClientBuilder restClientBuilder = RestClient.builder(httpHosts);
        restClientBuilder.setMaxRetryTimeoutMillis(elasticsearchProperties.getMaxRetryTimeout());

        // 请求参数设置
        restClientBuilder.setRequestConfigCallback(requestConfigBuilder ->
        {
            requestConfigBuilder.setConnectTimeout(elasticsearchProperties.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(elasticsearchProperties.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(elasticsearchProperties.getRequestTimeout());
            return requestConfigBuilder;
        });

        //异步 httpclient 连接参数配置
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder ->
        {
            httpClientBuilder.setMaxConnTotal(elasticsearchProperties.getMaxConnect());
            httpClientBuilder.setMaxConnPerRoute(elasticsearchProperties.getMaxConnectRoute());

            //                httpClientBuilder.setThreadFactory();
            // SSL 配置
            //                httpClientBuilder.setSSLContext()

            // 请求队列头部拦截 ，request response HttpResponseInterceptor HttpRequestInterceptor
            //            httpClientBuilder.addInterceptorFirst();
            // 请求队列尾部拦截 ，request response
            //            httpClientBuilder.addInterceptorLast();

            // 鉴权设置
            if (StringUtils.isNotBlank(elasticsearchProperties.getUsername()) && StringUtils
                    .isNotBlank(elasticsearchProperties.getPassword()))
            {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider
                        .setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticsearchProperties
                                .getUsername(), elasticsearchProperties.getPassword()));
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }

            return httpClientBuilder;
        });

        if (log.isDebugEnabled())
        {
            log.debug("初始化Elasticsearch Rest High Level Client 成功!");
        }
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        return restHighLevelClient;
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @exception Exception in case of shutdown errors.
     * Exceptions will get logged but not rethrown to allow
     * other beans to release their resources too.
     */
    @Override
    public void destroy() throws Exception
    {
        restHighLevelClient.close();
    }
}
