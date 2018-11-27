package com.xiao.custom.config.center.pojo.db.mapper;

import com.xiao.custom.config.center.pojo.db.entity.ApplicationItemGroupRelation;

/**
* Created by Mybatis Generator on 2018/11/27
*/
public interface ApplicationItemGroupRelationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ApplicationItemGroupRelation record);

    int insertSelective(ApplicationItemGroupRelation record);

    ApplicationItemGroupRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApplicationItemGroupRelation record);

    int updateByPrimaryKey(ApplicationItemGroupRelation record);
}