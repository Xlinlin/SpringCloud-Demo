package com.xiao.springcloud.rest.stater.autoconfig.config;/**
 * [简要描述]:
 * [详细描述]:
 *
 * @since JDK 1.8
 */

import com.xiao.springcloud.rest.stater.autoconfig.common.interceptor.RestInterceptor;
import com.xiao.springcloud.rest.stater.autoconfig.config.properties.HttpPoolProperties;
import com.xiao.springcloud.rest.stater.autoconfig.config.properties.OkHttpProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * [简要描述]: 初始化rest template
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/30 10:43
 * @since JDK 1.8
 */
@Configuration
@ComponentScan("com.xiao.springcloud.rest.stater.autoconfig")
@Slf4j
public class RestTemplateConfiguration
{

    @Bean
    @ConditionalOnProperty(value = "rest.okhttp.enable", havingValue = "true")
    @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    public ClientHttpRequestFactory okHttpHttpRequestFactory(OkHttpProperties okHttpProperties)
    {
        log.info("Init request factory for okHttp");
        ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
        //连接超时
        ((OkHttp3ClientHttpRequestFactory) clientHttpRequestFactory)
                .setConnectTimeout(okHttpProperties.getConnectionTimeout());
        //读超时
        ((OkHttp3ClientHttpRequestFactory) clientHttpRequestFactory).setReadTimeout(okHttpProperties.getReadTimeout());
        //写超时
        ((OkHttp3ClientHttpRequestFactory) clientHttpRequestFactory)
                .setWriteTimeout(okHttpProperties.getWriteTimeout());
        return clientHttpRequestFactory;
    }

    @Bean
    @ConditionalOnProperty(value = "rest.pool.enable", havingValue = "true")
    @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    public ClientHttpRequestFactory httpPoolRequestFactory(HttpPoolProperties httpPoolProperties)
    {
        log.info("Init request factory for http pool");
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(httpPoolProperties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(httpPoolProperties.getDefaultMaxPerRoute());
        connectionManager.setValidateAfterInactivity(httpPoolProperties.getValidateAfterInactivity());
        RequestConfig requestConfig = RequestConfig.custom()
                //服务器返回数据(response)的时间，超过抛出read timeout
                .setSocketTimeout(httpPoolProperties.getSocketTimeout())
                //连接上服务器(握手成功)的时间，超出抛出connect timeout
                .setConnectTimeout(httpPoolProperties.getConnectTimeout())
                //从连接池中获取连接的超时时间，超时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException: Timeout waiting for connection from pool
                .setConnectionRequestTimeout(httpPoolProperties.getConnectionRequestTimeout()).build();
        return new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build());
    }

    @Bean
    @ConditionalOnBean(ClientHttpRequestFactory.class)
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory)
    {
        log.info("Init rest template!");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setRequestFactory(clientHttpRequestFactory);

        //异常处理
        restTemplate.setErrorHandler(new ResponseErrorHandler()
        {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException
            {
                return response.getStatusCode().value() != 200;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException
            {
                log.error("Request error:");
                log.error("Response status code:" + response.getStatusCode().value());
            }
        });

        // 拦截
        restTemplate.setInterceptors(Collections.singletonList(new RestInterceptor()));
        return restTemplate;
    }
}
