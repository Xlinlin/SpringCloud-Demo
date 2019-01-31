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
public class Region {
    //
    private Long id;

    //区域名称
    private String regionName;

    //区域描述
    private String regionDesc;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;
}