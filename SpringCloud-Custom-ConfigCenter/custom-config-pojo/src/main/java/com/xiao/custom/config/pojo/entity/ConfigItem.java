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
public class ConfigItem
{
    //
    private Long id;

    //配置项KEY
    private String itemKey;

    //配置项值
    private String itemValue;

    //配置项描述
    private String itemDesc;

    //
    private Date createTime;

    //
    private Date updateTime;

    //0可用,1不可用
    private Integer status;

    //应用类型，0通用，1开发环境，2测试环境，3生产环境，4其他。默认通用类型
    private Integer itemType;
}