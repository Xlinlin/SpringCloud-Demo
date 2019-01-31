package com.xiao.custom.config.web.feign.config;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
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
 * @version 1.0, 2018/12/20 13:58
 * @since JDK 1.8
 */
@FeignClient(value = Constants.CONFIG_SERVICE)
@RequestMapping(value = "/configItem")
public interface ConfigItemFeign
{
    @RequestMapping(value = "/page")
    PageInfo<ConfigItemDto> pageConfigItem(@RequestBody ConfigItemQuery configItemQuery);

    @RequestMapping(value = "/batchDelete/{ids}")
    Integer enableOrDisable(@PathVariable("ids") String ids);

    @RequestMapping(value = "/save")
    Boolean save(@RequestBody ConfigItemDto configItemDto);

    @RequestMapping(value = "/update")
    Boolean update(@RequestBody ConfigItemDto configItemDto);
}
