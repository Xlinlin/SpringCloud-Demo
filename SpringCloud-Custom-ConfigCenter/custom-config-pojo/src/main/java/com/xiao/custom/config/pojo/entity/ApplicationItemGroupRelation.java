package com.xiao.custom.config.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* Created by Mybatis Generator on 2018/11/23
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationItemGroupRelation {
    //
    private Long id;

    //应用ID
    private Long applicationId;

    //配置组ID
    private Long itemGroupId;
}