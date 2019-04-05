package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.dto.RegionDto;
import com.xiao.custom.config.pojo.entity.Region;
import com.xiao.custom.config.pojo.query.RegionQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator on 2018/11/23
 */
public interface RegionMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(Region record);

    int insertSelective(Region record);

    Region selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Region record);

    int updateByPrimaryKey(Region record);

    /**
     * [简要描述]:通过条件查询<br/>
     * [详细描述]:<br/>
     *
     * @param regionQuery :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.RegionDto>
     * jun.liu  2018/11/26 - 17:27
     **/
    List<RegionDto> pageRegion(RegionQuery regionQuery);

    /**
     * [简要描述]:查询所有的区域
     * [详细描述]:<br/>
     *
     * @return java.util.List<com.winner.config.center.pojo.db.dto.RegionDto>
     * mjye  2018/12/21 - 16:51
     **/
    List<RegionDto> selectRegion();

    /**
     * [简要描述]:批量删除
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return int
     * mjye  2018/12/25 - 10:37
     **/
    int batchDelete(@Param("idArr") String[] idArr);
}