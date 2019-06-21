package com.xiao.springcloud.demo.common.gloab.interceptor.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.xiao.springcloud.demo.common.exception.CommonException;
import com.xiao.springcloud.demo.common.exception.CommonExceptionEnum;
import com.xiao.springcloud.demo.common.gloab.response.ErrorResponseData;
import com.xiao.springcloud.demo.common.gloab.response.ResponseData;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局的的异常拦截器（拦截所有的控制器）
 * （带有@RequestMapping注解的方法上都会拦截）
 *
 * @author zhdong
 */
@Slf4j
@ControllerAdvice
public class DefaultControllerAdvice
{

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 拦截common异常
     */
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData serviceException(CommonException e)
    {
        log.error("系统异常:", e);
        return new ErrorResponseData(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截common异常
     */
    @ExceptionHandler(HystrixRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData hystrixRuntimeException(HystrixRuntimeException e)
    {
        log.error("系统异常:", e);
        Throwable cause = e.getCause();
        //return new ErrorResponseData(e.getCode(), e.getErrorMessage());
        if (cause instanceof CommonException)
        {
            return serviceException((CommonException) cause);
        }
        cause = e.getFallbackException();
        if (null != cause)
        {
            log.error("服务调用熔断异常：", cause);
            // 解决服务之间调用，自定义熔断内抛出的异常处理
            if (null != cause.getCause())
            {
                Throwable e1 = cause.getCause().getCause();
                if (null != e1 && e1 instanceof CommonException)
                {
                    return serviceException((CommonException) e1);
                }
            }
        }
        return new ErrorResponseData(CommonExceptionEnum.REMOTE_SERVICE_NULL
                .getCode(), CommonExceptionEnum.REMOTE_SERVICE_NULL.getMessage());
    }

    /**
     * 拦截RetryableException异常
     */
    @ExceptionHandler(RetryableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData retryableException(RetryableException e)
    {
        log.error("系统异常:", e);
        return new ErrorResponseData(CommonExceptionEnum.REMOTE_SERVICE_TIMEOUT
                .getCode(), CommonExceptionEnum.REMOTE_SERVICE_TIMEOUT.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData notFount(Exception e, HttpServletResponse reponse)
    {
        if (HttpStatus.NOT_FOUND.value() == reponse.getStatus())
        {
            log.error(CommonExceptionEnum.NO_FOUNT.getMessage(), e);
            return new ErrorResponseData(CommonExceptionEnum.NO_FOUNT.getCode(), CommonExceptionEnum.NO_FOUNT
                    .getMessage());
        }
        log.error(CommonExceptionEnum.SYSTEM_ERROR.getMessage(), e);
        // 设置httpresponse头为500信息
        reponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ErrorResponseData(CommonExceptionEnum.SYSTEM_ERROR.getCode(), CommonExceptionEnum.SYSTEM_ERROR
                .getMessage());
    }

    //	@Override
    //	public boolean supports(MethodParameter methodParameter, Class aClass) {
    //		return true;
    //	}
    //
    //	/**
    //	 * 封装返回结果
    //	 * @param returnValue
    //	 * @param methodParameter
    //	 * @param mediaType
    //	 * @param aClass
    //	 * @param serverHttpRequest
    //	 * @param serverHttpResponse
    //	 * @return
    //	 */
    //	@Override
    //	public Object beforeBodyWrite(Object returnValue, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
    //
    //		//如果已经是ResponseData，直接返回
    //		if ( returnValue instanceof ResponseData) {
    //			return returnValue;
    //		}
    //		else if ( returnValue instanceof  String ){
    //			try {
    //				return objectMapper.writeValueAsString(SuccessResponseData.success(returnValue));
    //			}catch (Exception e){
    //				log.error("返回结果转换json异常",e);
    //				return ErrorResponseData.error("返回结果转换json异常");
    //			}
    //		}
    //
    //		return SuccessResponseData.success(returnValue);
    //	}

}
