package com.xiao.custom.config.service.service;

import com.xiao.custom.config.pojo.entity.ConfigItemGroupRelation;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:41
 * @since JDK 1.8
 */
public interface ConfigItemGroupRelationService
{
    Integer save(ConfigItemGroupRelation configItemGroupRelation);

    Integer update(ConfigItemGroupRelation configItemGroupRelation);

    void delete(Long id);

    /**
     * [简要描述]:批量绑定<br/>
     * [详细描述]:<br/>
     *
     * @param itemIdArr :
     * @param groupId   :
     * @return int
     * jun.liu  2018/11/28 - 9:31
     **/
    int batchSave(String[] itemIdArr, Long groupId);

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param itemIdArr :
     * @param groupId   :
     * @return int
     * jun.liu  2018/11/28 - 15:13
     **/
    int batchDelete(String[] itemIdArr, Long groupId);
}
