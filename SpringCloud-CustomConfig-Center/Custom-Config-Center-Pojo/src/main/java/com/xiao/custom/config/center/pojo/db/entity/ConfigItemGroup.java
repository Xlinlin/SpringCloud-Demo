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
public class ConfigItemGroup {
    //
    private Long id;

    //组名称
    private String groupName;

    //组描述
    private String groupDesc;

    //
    private Date creatTime;

    //
    private Date updateTime;
}