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
public class ConfigItemGroup {
    //
    private Long id;

    //组名称
    private String groupName;

    //组描述
    private String groupDesc;

    //
    private Date createTime;

    //
    private Date updateTime;
}