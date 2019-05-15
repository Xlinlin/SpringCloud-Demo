package com.xiao.custom.config.web.auth.exception;

import com.xiao.custom.config.web.auth.util.ResultCode;
import com.xiao.custom.config.web.auth.util.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Joetao
 * 异常处理类
 * controller层异常无法捕获处理，需要自己处理
 * Created at 2018/8/27.
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler
{

    /**
     * 处理所有自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public ResultJson handleCustomException(CustomException e)
    {
        log.error(e.getResultJson().getMsg().toString());
        return e.getResultJson();
    }

    /**
     * 处理参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultJson handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getBindingResult().getFieldError().getField() + e.getBindingResult().getFieldError()
                .getDefaultMessage());
        return ResultJson.failure(ResultCode.BAD_REQUEST, e.getBindingResult().getFieldError().getDefaultMessage());
    }
}
