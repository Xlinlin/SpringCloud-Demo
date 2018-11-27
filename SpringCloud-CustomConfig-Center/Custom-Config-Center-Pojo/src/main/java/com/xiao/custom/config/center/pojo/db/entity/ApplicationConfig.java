package com.xiao.custom.config.center.pojo.db.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Created by Mybatis Generator on 2018/11/27
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfig {
    //
    private Long id;

    //应用
    private String application;

    //应用名称
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