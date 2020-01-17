package com.xiao.custom.rest.starter.autoconfigure.config;

import com.xiao.custom.rest.starter.autoconfigure.config.properties.HttpPoolProperties;
import com.xiao.custom.rest.starter.autoconfigure.config.properties.OkHttpProperties;
import com.xiao.custom.rest.starter.autoconfigure.interceptor.RestInterceptor;
import com.xiao.custom.rest.starter.autoconfigure.service.HttpClientService;
import com.xiao.custom.rest.starter.autoconfigure.service.impl.HttpClientAsyncServiceImpl;
import com.xiao.custom.rest.starter.autoconfigure.service.impl.HttpClientServiceImpl;
import com.xiao.custom.rest.starter.autoconfigure.service.impl.HttpRetryService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

/**
 * [简要描述]: 初始化rest template
 * [详细描述]: 开启重试
 *
 * @author llxiao
 * @version 1.0, 2019/11/30 10:43
 * @since JDK 1.8
 */
@Configuration
@ComponentScan("com.xiao.custom.rest.starter.autoconfigure")
@Slf4j
@EnableRetry
public class RestTemplateConfiguration
{

    /**
     * [简要描述]:okHttp支持<br/>
     * [详细描述]:<br/>
     *
     * @param okHttpProperties :
     * @return org.springframework.http.client.ClientHttpRequestFactory
     * xiaolinlin  2020/1/16 - 18:43
     **/
    @Bean
    @ConditionalOnProperty(value = "rest.okhttp.enable", havingValue = "true")
    @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    public ClientHttpRequestFactory okHttpHttpRequestFactory(OkHttpProperties okHttpProperties)
    {
        log.info("Init request factory for okHttp!");
        OkHttp3ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory(buildOkHttpsClient()
                .build());
        //连接超时
        clientHttpRequestFactory.setConnectTimeout(okHttpProperties.getConnectionTimeout());
        //读超时
        clientHttpRequestFactory.setReadTimeout(okHttpProperties.getReadTimeout());
        //写超时
        clientHttpRequestFactory.setWriteTimeout(okHttpProperties.getWriteTimeout());
        return clientHttpRequestFactory;
    }

    /**
     * [简要描述]:http pool支持<br/>
     * [详细描述]:<br/>
     *
     * @param httpPoolProperties :
     * @return org.springframework.http.client.ClientHttpRequestFactory
     * xiaolinlin  2020/1/16 - 18:43
     **/
    @Bean
    @ConditionalOnProperty(value = "rest.pool.enable", havingValue = "true")
    @ConditionalOnMissingBean(ClientHttpRequestFactory.class)
    public ClientHttpRequestFactory httpPoolRequestFactory(HttpPoolProperties httpPoolProperties)
    {
        log.info("Init request factory for http pool");

        SSLConnectionSocketFactory socketFactory = null;
        SSLContext sslContext = buildSslContext();
        if (null != sslContext)
        {
            socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        }
        else
        {
            socketFactory = SSLConnectionSocketFactory.getSocketFactory();
        }
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", socketFactory)
                .build();

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

    /**
     * [简要描述]:RestTemplate<br/>
     * [详细描述]:<br/>
     *
     * @param clientHttpRequestFactory :
     * @return org.springframework.web.client.RestTemplate
     * xiaolinlin  2020/1/16 - 18:47
     **/
    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory)
    {
        log.info("Init rest template!");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setRequestFactory(clientHttpRequestFactory);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

        // 拦截
        restTemplate.setInterceptors(Collections.singletonList(new RestInterceptor()));
        return restTemplate;
    }

    /**
     * [简要描述]:集成重试机制<br/>
     * [详细描述]:<br/>
     *
     * @param restTemplate :
     * @return com.purcotton.omni.rest.stater.common.service.impl.HttpRetryService
     * xiaolinlin  2020/1/16 - 18:47
     **/
    @Bean
    public HttpRetryService retryService(RestTemplate restTemplate)
    {
        log.info("Init http retry support!");
        return new HttpRetryService(restTemplate);
    }

    /**
     * [简要描述]:http 同步服务<br/>
     * [详细描述]:<br/>
     *
     * @param retryService: 支持重试请求
     * @return com.purcotton.omni.rest.stater.common.service.HttpClientService
     * xiaolinlin  2020/1/16 - 18:48
     **/
    @Bean
    @ConditionalOnProperty(value = "rest.http.service.sync", havingValue = "true")
    @ConditionalOnMissingBean(HttpClientService.class)
    public HttpClientService httpClientService(HttpRetryService retryService)
    {
        log.info("Use sync http client service!");
        return new HttpClientServiceImpl(retryService);
    }

    /**
     * [简要描述]:http 异步服务<br/>
     * [详细描述]:<br/>
     *
     * @param retryService : 支持重试请求
     * @return com.purcotton.omni.rest.stater.common.service.HttpClientService
     * xiaolinlin  2020/1/16 - 18:48
     **/
    @Bean
    @ConditionalOnProperty(value = "rest.http.service.async", havingValue = "true")
    @ConditionalOnMissingBean(HttpClientService.class)
    public HttpClientService asyncHttpClientService(HttpRetryService retryService)
    {
        log.info("User async http client service!");
        return new HttpClientAsyncServiceImpl(retryService);
    }

    /**
     * [简要描述]:okhttp3 跳过https验证<br/>
     * [详细描述]:<br/>
     *
     * @return okhttp3.OkHttpClient.Builder
     * xiaolinlin  2020/1/4 - 10:31
     **/
    private OkHttpClient.Builder buildOkHttpsClient()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        TrustManager[] trustAllCerts = buildTrustManagers();
        SSLContext sslContext = buildSslContext();
        if (null != sslContext && null != trustAllCerts)
        {
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
        }
        builder.hostnameVerifier((hostname, session) -> true);
        return builder;
    }

    private SSLContext buildSslContext()
    {
        TrustManager[] trustAllCerts = buildTrustManagers();
        SSLContext sslContext = null;
        try
        {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        }
        catch (NoSuchAlgorithmException | KeyManagementException e)
        {
            log.error("Init SSLContext error :\n", e);
        }
        return sslContext;
    }

    private TrustManager[] buildTrustManagers()
    {
        return new TrustManager[] { new X509TrustManager()
        {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
            {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
            {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[] {};
            }
        }
        };
    }
}
