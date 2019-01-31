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
public class ConfigItemGroupRelation {
    //
    private Long id;

    //配置项ID
    private Long itemId;

    //组ID
    private Long groupId;
}