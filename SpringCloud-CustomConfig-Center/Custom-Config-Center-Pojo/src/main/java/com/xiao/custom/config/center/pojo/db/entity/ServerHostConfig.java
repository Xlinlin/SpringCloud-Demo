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
public class ServerHostConfig {
    //
    private Long id;

    //IP地址
    private String serverHost;

    //服务描述
    private String serverDesc;

    //关联区域
    private Long regionId;

    //
    private Date createTime;

    //
    private Date updateTime;
}