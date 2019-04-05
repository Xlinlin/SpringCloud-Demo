package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.entity.ConfigItemGroup;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator on 2018/11/23
*/
public interface ConfigItemGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfigItemGroup record);

    int insertSelective(ConfigItemGroup record);

    ConfigItemGroup selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfigItemGroup record);

    int updateByPrimaryKey(ConfigItemGroup record);

    /**
     * [简要描述]:通过条件查询<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/27 - 8:34
     **/
    List<ConfigItemGroupDto> pageConfigItemGroup(ConfigItemGroupQuery configItemGroupQuery);

    /**
     * [简要描述]:获取已绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 15:03
     **/
    List<ConfigItemGroupDto> pageRefGroupWithApp(ConfigItemGroupQuery configItemGroupQuery);

    /**
     * [简要描述]:获取未绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 15:32
     **/
    List<ConfigItemGroupDto> pageNotRefGroupWithApp(ConfigItemGroupQuery configItemGroupQuery);

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return int
     * jun.liu  2018/12/21 - 15:19
     **/
    int batchDelete(@Param("idArr") String[] idArr);
}