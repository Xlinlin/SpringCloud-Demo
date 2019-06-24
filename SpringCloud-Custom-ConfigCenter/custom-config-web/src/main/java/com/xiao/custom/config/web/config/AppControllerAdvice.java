package com.xiao.custom.config.web.config;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.xiao.custom.config.web.exception.ExceptionEnum;
import com.xiao.springcloud.demo.common.exception.CommonException;
import com.xiao.springcloud.demo.common.gloab.interceptor.advice.DefaultControllerAdvice;
import com.xiao.springcloud.demo.common.gloab.response.ErrorResponseData;
import com.xiao.springcloud.demo.common.gloab.response.ResponseData;
import com.xiao.springcloud.demo.common.gloab.response.SuccessResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局包裹返回值
 *
 * @author zhdong
 * Date 2018/9/2
 */
@Slf4j
@ControllerAdvice
public class AppControllerAdvice extends DefaultControllerAdvice implements ResponseBodyAdvice
{

    /**
     * 拦截服务调用异常
     */
    @ExceptionHandler(HystrixRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Override
    public ResponseData hystrixRuntimeException(HystrixRuntimeException e)
    {
        log.info("系统异常:", e);
        Throwable cause = e.getCause();
        //return new ErrorResponseData(e.getCode(), e.getErrorMessage());
        if (cause instanceof CommonException)
        {
            return serviceException((CommonException) cause);
        }
        log.info("服务远程调用异常:", e);
        return ErrorResponseData
                .error(ExceptionEnum.REQUEST_TIMEOUT.getCode(), ExceptionEnum.REQUEST_TIMEOUT.getMessage());
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass)
    {
        return true;
    }

    /**
     * 封装返回结果
     *
     * @param returnValue
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object returnValue, MethodParameter methodParameter, MediaType mediaType,
            Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse)
    {

        //如果已经是ResponseData，直接返回
        if (returnValue instanceof ResponseData)
        {
            return returnValue;
        }
        else if (returnValue instanceof String)
        {
            try
            {
                return JSONObject.toJSON(SuccessResponseData.success(returnValue));
            }
            catch (Exception e)
            {
                log.error("返回结果转换json异常", e);
                return ErrorResponseData.error("返回结果转换json异常");
            }
        }
        return SuccessResponseData.success(returnValue);
    }
}
