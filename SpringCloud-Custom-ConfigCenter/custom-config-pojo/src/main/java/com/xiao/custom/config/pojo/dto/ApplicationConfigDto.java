package com.xiao.custom.config.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* Created by Mybatis Generator on 2019/01/07
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationConfigDto
{
    //
    private Long id;

    //关联的应用ID
    private Long applicationId;

    //配置项KEY
    private String itemKey;

    //配置项值
    private String itemValue;

    //配置描述
    private String itemDesc;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;
}