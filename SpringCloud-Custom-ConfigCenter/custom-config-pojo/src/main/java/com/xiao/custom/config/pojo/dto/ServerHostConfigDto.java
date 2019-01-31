package com.xiao.custom.config.pojo.dto;

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
public class ServerHostConfigDto
{
    //
    private Long id;

    //IP地址
    private String serverHost;

    //服务描述
    private String serverDesc;

    //关联区域
    private Long regionId;

    //区域名称
    private String regionName;

    //
    private Date createTime;

    //
    private Date updateTime;
}