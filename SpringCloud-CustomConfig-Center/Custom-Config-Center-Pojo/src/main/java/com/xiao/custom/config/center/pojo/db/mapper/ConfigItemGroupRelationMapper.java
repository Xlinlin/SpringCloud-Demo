package com.xiao.custom.config.center.pojo.db.mapper;

import com.xiao.custom.config.center.pojo.db.entity.ConfigItemGroupRelation;

/**
* Created by Mybatis Generator on 2018/11/27
*/
public interface ConfigItemGroupRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfigItemGroupRelation record);

    int insertSelective(ConfigItemGroupRelation record);

    ConfigItemGroupRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfigItemGroupRelation record);

    int updateByPrimaryKey(ConfigItemGroupRelation record);
}