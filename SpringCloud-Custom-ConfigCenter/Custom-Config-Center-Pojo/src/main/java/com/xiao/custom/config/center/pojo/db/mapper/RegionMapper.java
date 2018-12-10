package com.xiao.custom.config.center.pojo.db.mapper;

import com.xiao.custom.config.center.pojo.db.entity.Region;

/**
* Created by Mybatis Generator on 2018/11/27
*/
public interface RegionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Region record);

    int insertSelective(Region record);

    Region selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Region record);

    int updateByPrimaryKey(Region record);
}