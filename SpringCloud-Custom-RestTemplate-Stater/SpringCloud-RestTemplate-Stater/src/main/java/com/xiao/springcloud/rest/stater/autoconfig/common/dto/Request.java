package com.xiao.springcloud.rest.stater.autoconfig.common.dto;

import lombok.Data;
import org.springframework.http.HttpHeaders;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/24 13:48
 * @since JDK 1.8
 */
@Data
public class Request
{
    public static final int POST = 0;
    public static final int JSON = 1;

    /**
     * 请求uri
     */
    private String uri;
    /**
     * 返回值类型
     */
    private Class responseType;
    /**
     * 请求参数
     */
    private Object params;
    /**
     * 执行方式：0普通请求，1.JSON请求
     */
    private int method;

    /**
     * 执行请求的ID，用于重复请求更新操作
     */
    private Long requestId;

    /**
     * 自定义请求头
     */
    private HttpHeaders headers;

    /**
     * url变量
     */
    private Object uriVariables;
}
