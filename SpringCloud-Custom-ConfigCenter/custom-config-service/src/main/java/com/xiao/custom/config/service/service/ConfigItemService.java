package com.xiao.custom.config.service.service;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.entity.ConfigItem;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:35
 * @since JDK 1.8
 */
public interface ConfigItemService
{
    Integer save(ConfigItem configItem);

    Integer update(ConfigItem configItem);

    void delete(Long id);

    PageInfo<ConfigItemDto> pageConfigItem(ConfigItemQuery configItemQuery, Integer pageNum, Integer pageSize);

    /**
     * [简要描述]:通过id获取<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return com.winner.config.center.pojo.db.entity.ConfigItem
     * jun.liu  2018/11/27 - 14:25
     **/
    ConfigItem getConfigItemById(Long id);

    /**
     * [简要描述]:分页获取已关联group的配置项<br/>
     * [详细描述]:<br/>
     *
     * @param configItemQuery :
     * @param pageNum :
     * @param pageSize :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/27 - 16:44
     **/
    PageInfo<ConfigItemDto> pageRefConfigItemWithGroup(ConfigItemQuery configItemQuery, int pageNum, int pageSize);

    /**
     * [简要描述]:分页获取未关联group的配置项<br/>
     * [详细描述]:<br/>
     *
     * @param configItemQuery :
     * @param pageNum :
     * @param pageSize :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/28 - 10:03
     **/
    PageInfo<ConfigItemDto> pageNotRefConfigItemWithGroup(ConfigItemQuery configItemQuery, int pageNum, int pageSize);

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param idArr :
     * @return int
     * jun.liu  2018/12/20 - 19:16
     **/
    int batchDelete(String[] idArr);
}
