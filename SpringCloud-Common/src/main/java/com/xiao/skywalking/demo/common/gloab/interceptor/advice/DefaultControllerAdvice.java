package com.xiao.skywalking.demo.common.gloab.interceptor.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.xiao.skywalking.demo.common.exception.CommonException;
import com.xiao.skywalking.demo.common.exception.CommonExceptionEnum;
import com.xiao.skywalking.demo.common.gloab.response.ErrorResponseData;
import com.xiao.skywalking.demo.common.gloab.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
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
        log.info("系统异常:", e);
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
        log.info("系统异常:", e);
        Throwable cause = e.getCause();
        //return new ErrorResponseData(e.getCode(), e.getErrorMessage());
        if (cause instanceof CommonException)
        {
            return serviceException((CommonException) cause);
        }
        return notFount(e);
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseData notFount(Exception e)
    {
        log.error("系统未知异常:", e);
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
