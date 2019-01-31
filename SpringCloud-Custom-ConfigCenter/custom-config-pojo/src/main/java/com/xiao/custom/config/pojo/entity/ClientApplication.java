package com.xiao.custom.config.pojo.entity;

import lombok.*;

import java.util.Date;

/**
 * t_client_application
 * Created by Mybatis Generator on 2019/01/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientApplication {
    private Long id;

    private String application;

    private Date createTime;

    private Date updateTime;

    private Integer status;

    private String profile;
}