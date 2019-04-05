package com.xiao.custom.config.pojo.entity;

import lombok.*;

import java.util.Date;

/**
 * t_client_host_info
 * Created by Mybatis Generator on 2019/01/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientHostInfo
{

    /**
     * 在线
     */
    public static final int ONLINE = 0;

    private Long id;

    private Long clientApplicationId;

    private String hostIp;

    private Integer hostPort;

    private String nettyIp;
    private Integer nettyPort;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}