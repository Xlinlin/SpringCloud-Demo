package com.xiao.spring.cloud.search.es.common;

import com.xiao.skywalking.demo.common.exception.AbstractServiceException;

/**
 * [简要描述]: 搜索服务异常枚举
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/9 10:12
 * @since JDK 1.8
 */
public enum SearchException implements AbstractServiceException
{
    SUCCESS(0, "成功!"),
    PARAM_IS_NULL(780000, "参数为空"),
    NOT_FOUND_DOC(780001, "商品找不到!"),
    ;

    SearchException(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 获取异常的状态码
     */
    @Override
    public Integer getCode()
    {
        return code;
    }

    /**
     * 获取异常的提示信息
     */
    @Override
    public String getMessage()
    {
        return message;
    }
}
