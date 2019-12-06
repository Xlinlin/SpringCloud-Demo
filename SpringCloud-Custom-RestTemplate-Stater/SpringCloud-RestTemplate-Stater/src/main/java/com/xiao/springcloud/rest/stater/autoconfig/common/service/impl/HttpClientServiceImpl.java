package com.xiao.springcloud.rest.stater.autoconfig.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xiao.springcloud.rest.stater.autoconfig.common.dto.Request;
import com.xiao.springcloud.rest.stater.autoconfig.common.log.annotation.RequestLog;
import com.xiao.springcloud.rest.stater.autoconfig.common.service.HttpClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * [简要描述]: http服务
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
 * @author llxiao
 * @version 1.0, 2019/4/24 09:25
 * @since JDK 1.8
 */
@Service
@Slf4j
public class HttpClientServiceImpl implements HttpClientService
{
    private static final String JSON_UTF_8 = "application/json; charset=UTF-8";
    private static final String HEADER_ACCEPT = "Accept";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * [简要描述]: 发起post请求<br/>
     * [详细描述]: @Retryable默认重试 等待1000ms 3次
     *
     * @param request : 请求参数
     * @return T
     * llxiao  2019/4/23 - 19:56
     **/
    @Override
    @RequestLog
    public <T> T doForObject(Request request)
    {
        ResponseEntity<T> entity = null;
        if (null != request)
        {
            String uri = request.getUri();
            Object params = request.getParams();
            int method = request.getMethod();
            Class responseType = request.getResponseType();
            HttpHeaders headers = request.getHeaders();
            Object uriVariables = request.getUriVariables();

            if (Request.POST == method)
            {
                entity = restTemplate.postForEntity(uri, params, responseType, uriVariables);
            }
            else if (Request.JSON == method)
            {
                if (null == headers)
                {
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType(JSON_UTF_8));
                    headers.add(HEADER_ACCEPT, MediaType.APPLICATION_JSON.toString());
                }
                HttpEntity<String> formEntity = new HttpEntity<>(JSONObject.toJSONString(params), headers);
                entity = restTemplate.postForEntity(uri, formEntity, responseType, uriVariables);
            }
            else
            {
                log.error("当期请求暂不支持的操作，请求参数：{}", JSONObject.toJSONString(request));
            }
        }
        return null == entity ? null : entity.getBody();
    }

    @Override
    @RequestLog
    public <T> T doRequest(Request request)
    {
        return (T) restTemplate.postForObject(request.getUri(), request.getParams(), request.getResponseType());
    }

    /**
     * [简要描述]: 不进行encode编码的get请求
     * [详细描述]: 请求参数中的url必须进行手动encode编码
     *
     * @param request : 请求参数
     * @return T
     * mjye  2019/10/23 - 11:32
     **/
    @Override
    @RequestLog
    public <T> T getForObject(Request request)
    {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request.getUri());
        return (T) restTemplate.getForObject(builder.build(true).toUri(), request.getResponseType());
    }

    /**
     * [简要描述]:formdata 获取请求Response<br/>
     * [详细描述]:<br/>
     *
     * @param request :
     * @return org.springframework.http.ResponseEntity<T>
     * llxiao  2019/8/26 - 16:33
     **/
    @Override
    @RequestLog
    public <T> ResponseEntity<T> postFormData(Request request)
    {
        HttpEntity httpEntity = null;
        Object params = request.getParams();
        // 请求参数为httpEntity直接发送请求
        if (params instanceof HttpEntity)
        {
            httpEntity = (HttpEntity) params;
        }
        // 需要重新组装 HttpEntity
        else if (params instanceof MultiValueMap)
        {
            MultiValueMap multiValueMap = (MultiValueMap) params;
            httpEntity = new HttpEntity<>(multiValueMap, request.getHeaders());
        }
        else if (params instanceof Map)
        {
            Map<String, String> parmasMap = (Map<String, String>) params;
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            for (Map.Entry<String, String> entry : parmasMap.entrySet())
            {
                multiValueMap.add(entry.getKey(), entry.getValue());
            }
            httpEntity = new HttpEntity<>(multiValueMap, request.getHeaders());
        }
        else
        {
            log.error("当亲请求暂不支持的操作请求，请求数据：{}", JSONObject.toJSONString(request));
        }
        if (null != httpEntity)
        {
            return restTemplate.postForEntity(request.getUri(), httpEntity, request.getResponseType());
        }
        else
        {
            log.error("请求异常，无法识别请求数据，请求数据：{}", JSONObject.toJSONString(request));
            return null;
        }

    }
}
