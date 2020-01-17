package com.xiao.custom.rest.starter.autoconfigure.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xiao.custom.rest.starter.autoconfigure.dto.Request;
import com.xiao.custom.rest.starter.autoconfigure.log.annotation.RequestLog;
import com.xiao.custom.rest.starter.autoconfigure.service.HttpClientService;
import com.xiao.custom.rest.starter.autoconfigure.util.RequestValidatorParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

/**
 * [简要描述]: http同步阻塞服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/24 09:25
 * @since JDK 1.8
 */
@Slf4j
public class HttpClientServiceImpl implements HttpClientService
{
    private static final String JSON_UTF_8 = "application/json; charset=UTF-8";

    private static final String HEADER_ACCEPT = "Accept";

    /**
     * 带重试机制
     */
    private HttpRetryService retryService;

    public HttpClientServiceImpl(HttpRetryService retryService)
    {
        this.retryService = retryService;
    }

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
        if (RequestValidatorParamsUtil.validateParams(request))
        {
            return null;
        }
        T entity = null;
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
                entity = retryService.postForObject(uri, params, responseType, uriVariables);
            }
            else if (Request.JSON == method)
            {
                if (null == headers)
                {
                    headers = new HttpHeaders();
                }
                headers.setContentType(MediaType.parseMediaType(JSON_UTF_8));
                headers.add(HEADER_ACCEPT, MediaType.APPLICATION_JSON.toString());
                HttpEntity<String> formEntity = new HttpEntity<>(JSONObject.toJSONString(params), headers);
                entity = retryService.postForObject(uri, formEntity, responseType, uriVariables);
            }
            else
            {
                log.error("当期请求暂不支持的操作，请求参数：{}", JSONObject.toJSONString(request));
            }
        }
        return entity;
    }

    /**
     * [简要描述]:普通HTTP请求<br/>
     * [详细描述]:喆道对接在使用<br/>
     *
     * @param request :
     * @return T
     * xiaolinlin  2020/1/16 - 18:38
     **/
    @Override
    @RequestLog
    public <T> T doRequest(Request request)
    {
        if (RequestValidatorParamsUtil.validateParams(request))
        {
            return null;
        }
        return retryService.doRequest(request.getUri(), request.getParams(), request.getResponseType());
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
        if (RequestValidatorParamsUtil.validateParams(request))
        {
            return null;
        }
        return retryService.getForObject(request.getUri(), request.getHeaders(), request.getResponseType());
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
        if (RequestValidatorParamsUtil.validateParams(request))
        {
            return null;
        }
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
            return retryService.postFormData(httpEntity, request.getUri(), request.getResponseType());
        }
        else
        {
            log.error("请求异常，无法识别请求数据，请求数据：{}", JSONObject.toJSONString(request));
            return null;
        }

    }
}
