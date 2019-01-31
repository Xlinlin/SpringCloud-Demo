package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.entity.ConfigItemGroupRelation;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Mybatis Generator on 2018/11/23
 */
public interface ConfigItemGroupRelationMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(ConfigItemGroupRelation record);

    int insertSelective(ConfigItemGroupRelation record);

    ConfigItemGroupRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfigItemGroupRelation record);

    int updateByPrimaryKey(ConfigItemGroupRelation record);

    /**
     * [简要描述]:批量绑定<br/>
     * [详细描述]:<br/>
     *
     * @param itemIdArr :
     * @param groupId :
     * @return int
     * jun.liu  2018/11/28 - 9:48
     **/
    int batchSave(@Param("itemIdArr") String[] itemIdArr, @Param("groupId") Long groupId);

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param itemIdArr :
     * @param groupId :
     * @return int
     * jun.liu  2018/11/28 - 15:13
     **/
    int batchDelete(@Param("itemIdArr") String[] itemIdArr, @Param("groupId") Long groupId);

    /**
     * [简要描述]:通过itemId批量删除配置项和配置组关联关系<br/>
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return int
     * jun.liu  2018/12/24 - 15:50
     **/
    int batchDeleteByItemId(@Param("idArr") String[] idArr);

    /**
     * [简要描述]:通过groupId批量删除配置项和配置组关联关系<br/>
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return void
     * jun.liu  2018/12/25 - 8:59
     **/
    int batchDeleteByGroupId(@Param("idArr") String[] idArr);

    /**
     * [简要描述]:统计配置项关联的配置组的数据<br/>
     * [详细描述]:<br/>
     *
     * @param itemId :
     * @return int
     * llxiao  2019/1/2 - 17:38
     **/
    int countByItemId(String itemId);
}