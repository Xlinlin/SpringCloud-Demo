package com.xiao.custom.config.center.pojo.db.mapper;

import com.xiao.custom.config.center.pojo.db.entity.ApplicationConfig;

/**
* Created by Mybatis Generator on 2018/11/27
*/
public interface ApplicationConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ApplicationConfig record);

    int insertSelective(ApplicationConfig record);

    ApplicationConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApplicationConfig record);

    int updateByPrimaryKey(ApplicationConfig record);
}