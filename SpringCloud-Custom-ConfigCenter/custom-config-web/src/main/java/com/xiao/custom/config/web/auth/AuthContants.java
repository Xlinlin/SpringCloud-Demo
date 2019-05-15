package com.xiao.custom.config.web.auth;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/9 14:55
 * @since JDK 1.8
 */
public interface AuthContants
{
    /**
     * 自定义token头
     */
    String TOKEN_HEADER = "M-Auth-Token";

    /**
     * token 开头
     */
    String TOKEN_BEARER_START = "Bearer ";

    /**
     * reqeust中存储username
     */
    String REQUEST_USER_NAME = "username";
}
