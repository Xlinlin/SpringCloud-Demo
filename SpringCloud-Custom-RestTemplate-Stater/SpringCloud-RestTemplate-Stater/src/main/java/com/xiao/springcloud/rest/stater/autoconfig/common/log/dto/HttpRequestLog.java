package com.xiao.springcloud.rest.stater.autoconfig.common.log.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * [简要描述]: 请求日志，以此来做请求补偿，请求日志记录等等
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/24 10:00
 * @since JDK 1.8
 */
@Data
public class HttpRequestLog
{
    public static final String REQUEST_LOG = "HttpRequestLog";

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 请求Url
     */
    private String uri;
    /**
     * 请求方式
     */
    private String method;
    /**
     * JSON键值对header
     */
    private String header;
    /**
     * 请求参数，JSON数据
     */
    private String params;
    /**
     * 响应参数，JSON数据
     */
    private String response;
    /**
     * 响应参数需要转换的类型
     */
    private String responseType;
    /**
     * http状态
     */
    private int httpStatus;
    /**
     * 请求最终状态
     */
    private int status;

    /**
     * 尝试次数
     */
    private int tryNum;

    /**
     * 整个请求request-JSON串
     */
    private String request;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 请求时间
     */
    private Timestamp requestTime;
    /**
     * 响应时间
     */
    private Timestamp responseTime;

    private Timestamp createTime;
    private Timestamp updateTime;
}
