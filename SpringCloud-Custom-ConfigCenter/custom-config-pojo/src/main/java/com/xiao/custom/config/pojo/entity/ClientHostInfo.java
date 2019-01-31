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
public class ClientHostInfo {
    private Long id;

    private Long clientApplicationId;

    private String hostIp;

    private Integer hostPort;

    private Integer status;

    private Date createTime;

    private Date updateTime;
}