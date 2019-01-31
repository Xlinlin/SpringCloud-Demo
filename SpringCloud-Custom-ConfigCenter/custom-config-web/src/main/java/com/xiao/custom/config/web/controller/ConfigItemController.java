package com.xiao.custom.config.web.controller;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;
import com.xiao.custom.config.web.feign.config.ConfigItemFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:配置项管理
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/12/20 11:43
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/api/config/configItem")
public class ConfigItemController
{
    @Autowired
    private ConfigItemFeign configItemService;

    @RequestMapping(value = "/page")
    public PageInfo<ConfigItemDto> pageConfigItem(@RequestBody ConfigItemQuery configItemQuery)
    {
        return configItemService.pageConfigItem(configItemQuery);
    }

    @RequestMapping(value = "/batchDelete/{ids}")
    public Integer enableOrDisable(@PathVariable("ids") String ids)
    {
        return configItemService.enableOrDisable(ids);
    }

    @RequestMapping(value = "/save")
    public Boolean save(@RequestBody ConfigItemDto configItemDto)
    {
        return configItemService.save(configItemDto);
    }

    @RequestMapping(value = "/update")
    public Boolean update(@RequestBody ConfigItemDto configItemDto)
    {
        return configItemService.update(configItemDto);
    }
}
