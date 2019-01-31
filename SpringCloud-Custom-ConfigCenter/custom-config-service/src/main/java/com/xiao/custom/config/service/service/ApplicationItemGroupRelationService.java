package com.xiao.custom.config.service.service;

import com.xiao.custom.config.pojo.entity.ApplicationItemGroupRelation;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 08:58
 * @since JDK 1.8
 */
public interface ApplicationItemGroupRelationService
{
    Integer save(ApplicationItemGroupRelation applicationItemGroupRelation);

    Integer update(ApplicationItemGroupRelation applicationItemGroupRelation);

    void delete(Long id);

    /**
     * [简要描述]:批量绑定<br/>
     * [详细描述]:<br/>
     *
     * @param groupIdArr :
     * @param appId      :
     * @return int
     * jun.liu  2018/11/28 - 15:38
     **/
    int batchSave(String[] groupIdArr, Long appId);

    /**
     * [简要描述]:删除绑定<br/>
     * [详细描述]:<br/>
     *
     * @param groupIdArr :
     * @param appId      :
     * @return int
     * jun.liu  2018/11/28 - 15:41
     **/
    int batchDelete(String[] groupIdArr, Long appId);
}
