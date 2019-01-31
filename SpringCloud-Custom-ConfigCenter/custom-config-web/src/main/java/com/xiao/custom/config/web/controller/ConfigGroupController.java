package com.xiao.custom.config.web.controller;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;
import com.xiao.custom.config.web.feign.config.ConfigGroupFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/12/21 14:26
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/api/config/configGroup")
public class ConfigGroupController
{
    @Autowired
    private ConfigGroupFeign configGroupFeign;

    @RequestMapping(value = "/page")
    public PageInfo<ConfigItemGroupDto> page(@RequestBody ConfigItemGroupQuery configItemGroupQuery)
    {
        return configGroupFeign.page(configItemGroupQuery);
    }

    @RequestMapping(value = "/delete/{ids}")
    public Integer delete(@PathVariable("ids") String ids)
    {
        return configGroupFeign.delete(ids);
    }

    @RequestMapping(value = "/save")
    public Boolean save(@RequestBody ConfigItemGroupDto configItemGroupDto)
    {
        return configGroupFeign.save(configItemGroupDto);
    }

    @RequestMapping(value = "/update")
    public Boolean update(@RequestBody ConfigItemGroupDto configItemGroupDto)
    {
        return configGroupFeign.update(configItemGroupDto);
    }

    @RequestMapping(value = "/isRefGroup")
    public PageInfo<ConfigItemDto> pageRefConfigItemWithGroup(@RequestBody ConfigItemQuery configItemQuery)
    {
        return configGroupFeign.pageRefConfigItemWithGroup(configItemQuery);
    }

    @RequestMapping(value = "/batchDelete/{groupId}/{itemIds}")
    public Boolean batchDelete(@PathVariable("itemIds") String itemIds, @PathVariable("groupId") Long groupId)
    {
        return configGroupFeign.batchDelete(groupId, itemIds);
    }

    @RequestMapping(value = "/notRefGroup")
    public PageInfo<ConfigItemDto> pageNotRefConfigItemWithGroup(@RequestBody ConfigItemQuery configItemQuery)
    {
        return configGroupFeign.pageNotRefConfigItemWithGroup(configItemQuery);
    }

    @RequestMapping(value = "/batchSave/{groupId}/{itemIds}")
    public Boolean batchSave(@PathVariable("itemIds") String itemIds, @PathVariable("groupId") Long groupId)
    {
        return configGroupFeign.batchSave(groupId, itemIds);
    }
}
