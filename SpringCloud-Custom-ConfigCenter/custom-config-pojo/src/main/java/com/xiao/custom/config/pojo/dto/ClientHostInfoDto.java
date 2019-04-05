package com.xiao.custom.config.pojo.dto;

import lombok.Data;

import java.util.Date;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/27 14:23
 * @since JDK 1.8
 */
@Data
public class ClientHostInfoDto
{
    private Long id;

    /**
     * 应用ID
     */
    private Long applicationClientId;

    /**
     * 应用
     */
    private String application;

    private String nettyIp;
    private Integer nettyPort;

    private String hostIp;

    private Integer hostPort;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}
