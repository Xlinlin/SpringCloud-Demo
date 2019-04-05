package com.xiao.custom.config.service.service;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.entity.ConfigItemGroup;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 17:51
 * @since JDK 1.8
 */
public interface ConfigItemGroupService
{
    Integer save(ConfigItemGroup configItemGroup);

    Integer update(ConfigItemGroup configItemGroup);

    Integer delete(Long id);

    PageInfo<ConfigItemGroupDto> pageConfigItemGroup(ConfigItemGroupQuery configItemGroupQuery, Integer pageNum,
            Integer pageSize);

    /**
     * [简要描述]:通过id获取<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return com.winner.config.center.pojo.db.entity.ConfigItemGroup
     * jun.liu  2018/11/27 - 16:14
     **/
    ConfigItemGroup getConfigItemGroupById(Long id);

    /**
     * [简要描述]:获取已绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @param pageNum              :
     * @param pageSize             :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 15:02
     **/
    PageInfo<ConfigItemGroupDto> pageRefGroupWithApp(ConfigItemGroupQuery configItemGroupQuery, int pageNum,
            int pageSize);

    /**
     * [简要描述]:获取未绑定该应用的配置组<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @param pageNum              :
     * @param pageSize             :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/28 - 15:02
     **/
    PageInfo<ConfigItemGroupDto> pageNotRefGroupWithApp(ConfigItemGroupQuery configItemGroupQuery, int pageNum,
            int pageSize);

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return int
     * jun.liu  2018/12/21 - 15:18
     **/
    int batchDelete(String[] idArr);
}
