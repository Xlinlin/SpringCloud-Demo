package com.xiao.custom.rest.starter.autoconfigure.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.SocketTimeoutException;

/**
 * [简要描述]: 重试不能使用接口实现类
 * [详细描述]:
 * 重试机制注解说明（https://blog.csdn.net/u011116672/article/details/77823867）：
 * EnableRetry注解：
 * 能否重试，proxyTargetClass属性为true时（默认false），使用CGLIB代理
 * <p>
 * Retryable注解：注解需要被重试的方法
 * include 指定处理的异常类。默认为空
 * exclude指定不需要处理的异常。默认为空
 * vaue指定要重试的异常。默认为空
 * maxAttempts 最大重试次数。默认3次
 * backoff 重试等待策略。默认使用@Backoff注解
 * <p>
 * Backoff注解：重试回退策略（立即重试还是等待一会再重试）
 * 不设置参数时，默认使用FixedBackOffPolicy，重试等待1000ms
 * 只设置delay()属性时，使用FixedBackOffPolicy，重试等待指定的毫秒数
 * 当设置delay()和maxDealy()属性时，重试等待在这两个值之间均态分布
 * 使用delay()，maxDealy()和multiplier()属性时，使用ExponentialBackOffPolicy
 * 当设置multiplier()属性不等于0时，同时也设置了random()属性时，使用ExponentialRandomBackOffPolicy
 * <p>
 * Recover注解: 用于方法。
 * 用于@Retryable失败时的“兜底”处理方法。 @Recover注释的方法必须要与@Retryable注解的方法“签名”保持一致，第一入参为要重试的异常，其他参数与@Retryable保持一致，返回值也要一样，否则无法执行！
 * <p>
 * CircuitBreaker注解：用于方法，实现熔断模式。
 * include 指定处理的异常类。默认为空
 * exclude指定不需要处理的异常。默认为空
 * vaue指定要重试的异常。默认为空
 * maxAttempts 最大重试次数。默认3次
 * openTimeout 配置熔断器打开的超时时间，默认5s，当超过openTimeout之后熔断器电路变成半打开状态（只要有一次重试成功，则闭合电路）
 * resetTimeout 配置熔断器重新闭合的超时时间，默认20s，超过这个时间断路器关闭
 *
 * @author xiaolinlin
 * @version 1.0, 2020/1/16 17:45
 * @since JDK 1.8
 */
public class HttpRetryService
{
    private RestTemplate restTemplate;

    public HttpRetryService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    /**
     * [简要描述]:发起post请求<br/>
     * [详细描述]:<br/>
     *
     * @param uri :
     * @param params :
     * @param responseType :
     * @param uriVariables :
     * @return T
     * xiaolinlin  2020/1/16 - 18:20
     **/
    @Retryable(value = SocketTimeoutException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public <T> T postForObject(String uri, Object params, Class responseType, Object uriVariables)
    {
        if (StringUtils.isNotBlank(uri) && null != params && null != responseType)
        {
            ResponseEntity<T> responseEntity = restTemplate.postForEntity(uri, params, responseType, uriVariables);
            return null == responseEntity ? null : responseEntity.getBody();
        }
        return null;
    }

    /**
     * [简要描述]:formdata 获取请求Response<br/>
     * [详细描述]:<br/>
     *
     * @param httpEntity :
     * @param uri :
     * @param responseType :
     * @return org.springframework.http.ResponseEntity<T>
     * xiaolinlin  2020/1/16 - 18:27
     **/
    @Retryable(value = SocketTimeoutException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public <T> ResponseEntity<T> postFormData(HttpEntity httpEntity, String uri, Class responseType)
    {
        if (null != httpEntity && StringUtils.isNotBlank(uri) && null != responseType)
        {
            return restTemplate.postForEntity(uri, httpEntity, responseType);
        }
        return null;
    }

    /**
     * [简要描述]:不进行encode编码的get请求<br/>
     * [详细描述]:<br/>
     *
     * @param uri :
     * @param headers :
     * @param responseType :
     * @return T
     * xiaolinlin  2020/1/16 - 18:33
     **/
    @Retryable(value = SocketTimeoutException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public <T> T getForObject(String uri, HttpHeaders headers, Class responseType)
    {
        if (StringUtils.isNotBlank(uri) && null != responseType)
        {
            HttpEntity<String> requestEntity = null;
            if (null != headers)
            {
                requestEntity = new HttpEntity<>(null, headers);
            }
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
            ResponseEntity<T> resEntity = restTemplate
                    .exchange(builder.build(true).toUri(), HttpMethod.GET, requestEntity, responseType);
            return resEntity.getBody();
        }
        return null;

    }

    /**
     * [简要描述]:普通post请求<br/>
     * [详细描述]:<br/>
     *
     * @param uri :
     * @param params :
     * @param responseType :
     * @return T
     * xiaolinlin  2020/1/16 - 18:37
     **/
    @Retryable(value = SocketTimeoutException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public <T> T doRequest(String uri, Object params, Class responseType)
    {
        if (StringUtils.isNotBlank(uri) && null != params && null != responseType)
        {
            return (T) restTemplate.postForObject(uri, params, responseType);
        }
        return null;
    }
}
