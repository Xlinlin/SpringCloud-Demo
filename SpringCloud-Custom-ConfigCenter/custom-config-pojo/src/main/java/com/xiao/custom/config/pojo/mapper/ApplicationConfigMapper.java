package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.dto.ApplicationConfigDto;
import com.xiao.custom.config.pojo.entity.ApplicationConfig;
import com.xiao.custom.config.pojo.query.ApplicationConfigQuery;

import java.util.List;

/**
 * 应用对应的私有配置属性
 * Created by Mybatis Generator on 2019/01/07
 */
public interface ApplicationConfigMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(ApplicationConfig record);

    int insertSelective(ApplicationConfig record);

    ApplicationConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApplicationConfig record);

    int updateByPrimaryKey(ApplicationConfig record);

    /**
     * [简要描述]:分页查询应用关联的私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param query :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ApplicationConfigDto>
     * llxiao  2019/1/7 - 15:27
     **/
    List<ApplicationConfigDto> pageQuery(ApplicationConfigQuery query);
}