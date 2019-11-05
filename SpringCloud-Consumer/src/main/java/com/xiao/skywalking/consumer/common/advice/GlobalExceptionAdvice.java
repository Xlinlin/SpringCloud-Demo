package com.xiao.skywalking.consumer.common.advice;

import com.xiao.skywalking.consumer.common.CommonException;
import com.xiao.skywalking.consumer.common.ExceptionEnum;
import com.xiao.skywalking.consumer.common.response.ErrorResponseData;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ocsp.ResponseData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/2 09:54
 * @since JDK 1.8
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionAdvice
{
    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(CommonException.class)
    @ResponseBody
    public ResponseData serviceException(CommonException e)
    {
        log.error("服务端业务异常:", e.getErrorMessage());
        return new ErrorResponseData(e.getCode(), e.getErrorMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData notFount(Exception e)
    {
        printRequestUrl();
        log.error("请求出现系统异常，异常信息：", e);
        return new ErrorResponseData(ExceptionEnum.SYSTEM_ERROR.getErrorCode(), ExceptionEnum.SYSTEM_ERROR
                .getErrorMsg());
    }

    /**
     * 打印请求异常
     */
    private void printRequestUrl()
    {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (null != requestAttributes)
        {
            HttpServletRequest request = requestAttributes.getRequest();
            if (null != request)
            {
                log.error("客户端IP：{}发起请求URL:{}出现未知异常", request.getRemoteAddr(), request.getRequestURL());
            }
        }
    }
}
