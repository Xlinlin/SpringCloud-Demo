package com.xiao.custom.config.client.netty.dto;

import lombok.Data;

/**
 * [简要描述]: Netty交互消息体
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/30 10:42
 * @since JDK 1.8
 */
@Data
public class Message
{
    /**
     * 0：心跳，1：登录以及绑定NETTY信息，2：刷新
     */
    private int command;

    /**
     * 消息体
     */
    private String message;

    /**
     * 返回状态
     */
    private int status;

    /**
     * 错误消息
     */
    private String errorMessage;

    /**
     * 客户端服务端口
     */
    private int serverPort;

    /**
     * 客户端服务IP
     */
    private String hostIp;

    /**
     * 应用名称
     */
    private String applicationName;
}
