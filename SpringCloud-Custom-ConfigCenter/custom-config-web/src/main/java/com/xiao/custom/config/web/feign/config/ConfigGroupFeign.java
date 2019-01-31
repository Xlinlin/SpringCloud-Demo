package com.xiao.custom.config.web.feign.config;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;
import com.xiao.custom.config.web.commo.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/12/21 14:26
 * @since JDK 1.8
 */
@FeignClient(value = Constants.CONFIG_SERVICE)
@RequestMapping(value = "/configItemGroup")
public interface ConfigGroupFeign
{
    @RequestMapping(value = "/page")
    PageInfo<ConfigItemGroupDto> page(@RequestBody ConfigItemGroupQuery configItemGroupQuery);

    @RequestMapping(value = "/delete/{ids}")
    Integer delete(@PathVariable("ids") String ids);

    @RequestMapping(value = "/save")
    Boolean save(@RequestBody ConfigItemGroupDto configItemGroupDto);

    @RequestMapping(value = "/update")
    Boolean update(@RequestBody ConfigItemGroupDto configItemGroupDto);

    @RequestMapping(value = "/isRefGroup")
    PageInfo<ConfigItemDto> pageRefConfigItemWithGroup(@RequestBody ConfigItemQuery configItemQuery);

    @RequestMapping(value = "/batchDelete/{groupId}/{itemIds}")
    Boolean batchDelete(@PathVariable("groupId") Long groupId, @PathVariable("itemIds") String itemIds);

    @RequestMapping(value = "/notRefGroup")
    PageInfo<ConfigItemDto> pageNotRefConfigItemWithGroup(@RequestBody ConfigItemQuery configItemQuery);

    @RequestMapping(value = "/batchSave/{groupId}/{itemIds}")
    Boolean batchSave(@PathVariable("groupId") Long groupId, @PathVariable("itemIds") String itemIds);
}
