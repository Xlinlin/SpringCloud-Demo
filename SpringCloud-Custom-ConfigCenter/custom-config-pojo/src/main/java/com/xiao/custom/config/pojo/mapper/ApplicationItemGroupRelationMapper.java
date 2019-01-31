package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.entity.ApplicationItemGroupRelation;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Mybatis Generator on 2018/11/23
 */
public interface ApplicationItemGroupRelationMapper
{
    int deleteByPrimaryKey(Long id);

    int insert(ApplicationItemGroupRelation record);

    int insertSelective(ApplicationItemGroupRelation record);

    ApplicationItemGroupRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApplicationItemGroupRelation record);

    int updateByPrimaryKey(ApplicationItemGroupRelation record);

    /**
     * [简要描述]:绑定应用与多个配置组<br/>
     * [详细描述]:<br/>
     *
     * @param groupIdArr :
     * @param appId :
     * @return void
     * jun.liu  2018/11/28 - 14:10
     **/
    int batchSave(@Param("groupIdArr") String[] groupIdArr, @Param("appId") Long appId);

    /**
     * [简要描述]:删除绑定<br/>
     * [详细描述]:<br/>
     *
     * @param groupIdArr :
     * @param appId :
     * @return int
     * jun.liu  2018/11/28 - 15:41
     **/
    int batchDelete(@Param("groupIdArr") String[] groupIdArr, @Param("appId") Long appId);

    /**
     * [简要描述]:应用ID删除关联配置信息<br/>
     * [详细描述]:<br/>
     *
     * @param appId : 应用ID
     * @return int
     * llxiao  2018/12/24 - 10:34
     **/
    int deleteByAppId(@Param("appId") Long appId);

    /**
     * [简要描述]:组ID统计关联的应用数量<br/>
     * [详细描述]:<br/>
     *
     * @param groupId :
     * @return int
     * llxiao  2019/1/2 - 17:24
     **/
    int countByGroupId(String groupId);
}