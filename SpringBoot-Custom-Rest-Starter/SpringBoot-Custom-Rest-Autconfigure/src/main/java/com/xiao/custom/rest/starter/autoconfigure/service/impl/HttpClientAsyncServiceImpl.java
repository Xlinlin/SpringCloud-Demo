package com.xiao.custom.rest.starter.autoconfigure.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xiao.custom.rest.starter.autoconfigure.dto.Request;
import com.xiao.custom.rest.starter.autoconfigure.log.annotation.RequestLog;
import com.xiao.custom.rest.starter.autoconfigure.service.HttpClientService;
import com.xiao.custom.rest.starter.autoconfigure.util.RequestValidatorParamsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * [简要描述]: CompletableFuture实现http异步服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/24 09:25
 * @since JDK 1.8
 */
@Slf4j
public class HttpClientAsyncServiceImpl implements HttpClientService
{
    private HttpClientServiceImpl httpClientService;

    public HttpClientAsyncServiceImpl(HttpRetryService retryService)
    {
        this.httpClientService = new HttpClientServiceImpl(retryService);
    }

    /**
     * [简要描述]: 发起post请求<br/>
     * [详细描述]: @Retryable默认重试 等待2000ms 3次
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
        CompletableFuture<T> tCompletableFuture = CompletableFuture
                .supplyAsync(() -> httpClientService.doForObject(request));
        return futureResult(tCompletableFuture, request);
    }

    @Override
    @RequestLog
    public <T> T doRequest(Request request)
    {
        if (RequestValidatorParamsUtil.validateParams(request))
        {
            return null;
        }
        CompletableFuture<T> tCompletableFuture = CompletableFuture
                .supplyAsync(() -> httpClientService.doRequest(request));
        return futureResult(tCompletableFuture, request);
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
        CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> httpClientService.doForObject(request));
        return futureResult(future, request);
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
        CompletableFuture<ResponseEntity<T>> completableFuture = CompletableFuture
                .supplyAsync(() -> httpClientService.postFormData(request));
        return futureResult(completableFuture, request);
    }

    private <T> T futureResult(CompletableFuture<T> tCompletableFuture, Request request)
    {
        try
        {
            return tCompletableFuture.get();
        }
        catch (InterruptedException e)
        {
            log.error("请求参数：{}", JSONObject.toJSONString(request));
            log.error("Http异步请求线程中断:", e);
        }
        catch (ExecutionException e)
        {
            log.error("请求参数：{}", JSONObject.toJSONString(request));
            log.error("Http异步请求异常:", e);
        }
        return null;
    }
}
