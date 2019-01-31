package com.xiao.custom.config.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* Created by Mybatis Generator on 2018/11/23
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application
{
    //
    private Long id;

    //应用
    private String application;

    //应用描述
    private String applicationName;

    //
    private String label;

    //环境
    private String profile;

    //
    private Date createTime;

    //
    private Date updateTime;

    //所属区域
    private Long regionId;
}