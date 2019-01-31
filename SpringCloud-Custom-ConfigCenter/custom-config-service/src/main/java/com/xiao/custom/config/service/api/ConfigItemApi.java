package com.xiao.custom.config.service.api;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.entity.ConfigItem;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;
import com.xiao.custom.config.service.service.ConfigItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 10:44
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/configItem")
@Slf4j
public class ConfigItemApi
{
    @Autowired
    private ConfigItemService configItemService;

    /**
     * [简要描述]:分页获取<br/>
     * [详细描述]:<br/>
     *
     * @param configItemQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/27 - 10:54
     **/
    @RequestMapping(value = "/page")
    public PageInfo<ConfigItemDto> pageConfigItem(@RequestBody ConfigItemQuery configItemQuery)
    {
        return configItemService
                .pageConfigItem(configItemQuery, configItemQuery.getPageNum(), configItemQuery.getPageSize());
    }

    /**
     * [简要描述]:新增<br/>
     * [详细描述]:<br/>
     *
     * @param configItemDto :
     * @return java.lang.Boolean
     * jun.liu  2018/11/27 - 13:53
     **/
    @RequestMapping(value = "/save")
    public Boolean save(@RequestBody ConfigItemDto configItemDto)
    {
        if (StringUtils.isBlank(configItemDto.getItemKey()) || StringUtils.isBlank(configItemDto.getItemValue()))
        {
            log.info("新增配置项失败,参数不能为空");
            throw new RuntimeException("参数不能为空");
        }
        configItemDto.setCreateTime(new Date());
        int a = configItemService.save(ConfigItemDto.convertToEntity(configItemDto));
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:更新<br/>
     * [详细描述]:<br/>
     *
     * @param configItemDto :
     * @return java.lang.Boolean
     * jun.liu  2018/11/27 - 14:06
     **/
    @RequestMapping(value = "/update")
    public Boolean update(@RequestBody ConfigItemDto configItemDto)
    {
        if (null == configItemDto.getId() || StringUtils.isBlank(configItemDto.getItemKey()) || StringUtils
                .isBlank(configItemDto.getItemValue()))
        {
            log.info("修改配置项失败,id:{}、key:{}、value:{}不能为空", configItemDto.getId(), configItemDto
                    .getItemKey(), configItemDto.getItemValue());
            throw new RuntimeException("修改配置项失败,参数不能为空");
        }
        ConfigItem configItem = configItemService.getConfigItemById(configItemDto.getId());
        if (configItem == null)
        {
            log.info("通过id:" + configItemDto.getId() + ",获取configItem失败");
            throw new RuntimeException("获取对象失败");
        }
        configItem.setItemValue(configItemDto.getItemValue());
        configItem.setItemKey(configItemDto.getItemKey());
        configItem.setItemDesc(configItemDto.getItemDesc());
        configItem.setItemType(configItemDto.getItemType());
        configItem.setUpdateTime(new Date());
        int a = configItemService.update(configItem);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:删除配置项<br/>
     * [详细描述]:<br/>
     *
     * @param ids :
     * @return java.lang.Boolean
     * jun.liu  2018/11/27 - 14:39
     **/
    @RequestMapping(value = "/batchDelete/{ids}")
    public Integer enableOrDisable(@PathVariable("ids") String ids)
    {
        if (StringUtils.isBlank(ids))
        {
            log.info("启用/禁用配置项失败,数据为空");
            throw new RuntimeException("数据不能为空");
        }
        String[] idArr = ids.split(",");
        int a = configItemService.batchDelete(idArr);
        return idArr.length - a;
    }
}
