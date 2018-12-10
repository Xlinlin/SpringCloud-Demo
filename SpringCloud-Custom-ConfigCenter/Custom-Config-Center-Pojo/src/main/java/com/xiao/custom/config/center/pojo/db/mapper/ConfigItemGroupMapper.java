package com.xiao.custom.config.center.pojo.db.mapper;

import com.xiao.custom.config.center.pojo.db.entity.ConfigItemGroup;

/**
* Created by Mybatis Generator on 2018/11/27
*/
public interface ConfigItemGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfigItemGroup record);

    int insertSelective(ConfigItemGroup record);

    ConfigItemGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfigItemGroup record);

    int updateByPrimaryKey(ConfigItemGroup record);
}