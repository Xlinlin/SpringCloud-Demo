package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.entity.ConfigItem;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator on 2018/11/23
*/
public interface ConfigItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfigItem record);

    int insertSelective(ConfigItem record);

    ConfigItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfigItem record);

    int updateByPrimaryKey(ConfigItem record);

    /**
     * [简要描述]:通过条件查询<br/>
     * [详细描述]:<br/>
     *
     * @param configItemQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/27 - 9:40
     **/
    List<ConfigItemDto> pageConfigItem(ConfigItemQuery configItemQuery);

    /**
     * [简要描述]:分页获取已关联group的配置项<br/>
     * [详细描述]:<br/>
     *
     * @param configItemQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/27 - 17:05
     **/
    List<ConfigItemDto> pageRefConfigItemWithGroup(ConfigItemQuery configItemQuery);

    /**
     * [简要描述]:分页获取未关联group的配置项<br/>
     * [详细描述]:<br/>
     *
     * @param configItemQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/28 - 10:04
     **/
    List<ConfigItemDto> pageNotRefConfigItemWithGroup(ConfigItemQuery configItemQuery);

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return int
     * jun.liu  2018/12/20 - 19:17
     **/
    int batchDelete(@Param("idArr") String[] idArr);
}