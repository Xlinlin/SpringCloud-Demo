package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.dto.ApplicationDto;
import com.xiao.custom.config.pojo.entity.Application;
import com.xiao.custom.config.pojo.query.AppQuery;

import java.util.List;

/**
 * Created by Mybatis Generator on 2018/11/23
 */
public interface ApplicationMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(Application record);

    int insertSelective(Application record);

    Application selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Application record);

    int updateByPrimaryKey(Application record);

    /**
     * [简要描述]:根据条件查询<br/>
     * [详细描述]:<br/>
     *
     * @param appQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.entity.ApplicationConfig>
     * jun.liu  2018/11/26 - 17:08
     **/
    List<ApplicationDto> pageApplicationConfig(AppQuery appQuery);

    /**
     * [简要描述]:区域ID统计应用<br/>
     * [详细描述]:<br/>
     *
     * @param regionId :
     * @return int
     * llxiao  2019/1/2 - 17:55
     **/
    Integer countByRegionId(Long regionId);
}