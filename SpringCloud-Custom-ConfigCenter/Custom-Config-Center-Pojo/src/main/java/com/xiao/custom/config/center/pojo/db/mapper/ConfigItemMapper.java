package com.xiao.custom.config.center.pojo.db.mapper;

import com.xiao.custom.config.center.pojo.db.entity.ConfigItem;

/**
* Created by Mybatis Generator on 2018/11/27
*/
public interface ConfigItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfigItem record);

    int insertSelective(ConfigItem record);

    ConfigItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfigItem record);

    int updateByPrimaryKey(ConfigItem record);
}