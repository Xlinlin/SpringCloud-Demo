package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.dto.ServerHostConfigDto;
import com.xiao.custom.config.pojo.entity.ServerHostConfig;
import com.xiao.custom.config.pojo.query.ServerHostConfigQuery;

import java.util.List;

/**
* Created by Mybatis Generator on 2018/11/23
*/
public interface ServerHostConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ServerHostConfig record);

    int insertSelective(ServerHostConfig record);

    ServerHostConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ServerHostConfig record);

    int updateByPrimaryKey(ServerHostConfig record);

    /**
     * [简要描述]:通过条件查询<br/>
     * [详细描述]:<br/>
     *
     * @param serverHostConfigQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ServerHostConfigDto>
     * jun.liu  2018/11/27 - 9:51
     **/
    List<ServerHostConfigDto> pageServerHostConfig(ServerHostConfigQuery serverHostConfigQuery);
}