package com.xiao.skywalking.consumer.common;/**
 * [简要描述]:
 * [详细描述]:
 *
 * @since JDK 1.8
 */

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/1 11:10
 * @since JDK 1.8
 */
public enum ExceptionEnum
{
    /**
     * 非法参数
     */
    INVALID_PARAMETER(6000, "非法参数"),

    /**
     * 当天当前活动用户已经中奖
     */
    USER_PRIZE_EXIST(6001, "当天当前活动用户已经中奖"),

    /**
     * 系统未知异常
     */
    SYSTEM_ERROR(9999, "系统异常");
    /**
     * 错误状态码
     */
    private int errorCode;
    /**
     * 错误消息
     */
    private String errorMsg;

    ExceptionEnum(int errorCode, String errorMsg)
    {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }
}
