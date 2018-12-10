package com.xiao.custom.config.center.pojo.db.mapper;

import com.xiao.custom.config.center.pojo.db.entity.ServerHostConfig;

/**
* Created by Mybatis Generator on 2018/11/27
*/
public interface ServerHostConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServerHostConfig record);

    int insertSelective(ServerHostConfig record);

    ServerHostConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ServerHostConfig record);

    int updateByPrimaryKey(ServerHostConfig record);
}