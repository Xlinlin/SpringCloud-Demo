package com.xiao.skywalking.consumer.common;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/1 11:08
 * @since JDK 1.8
 */
public class CommonException extends RuntimeException
{
    private Integer code;

    private String errorMessage;

    public CommonException(Integer code, String errorMessage)
    {
        super(errorMessage);
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public CommonException(ExceptionEnum exceptionEnum)
    {
        super(exceptionEnum.getErrorMsg());
        this.code = exceptionEnum.getErrorCode();
        this.errorMessage = exceptionEnum.getErrorMsg();
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
