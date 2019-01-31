package com.xiao.custom.config.service.api;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.entity.ConfigItemGroup;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;
import com.xiao.custom.config.service.service.ConfigItemGroupRelationService;
import com.xiao.custom.config.service.service.ConfigItemGroupService;
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
 * @version 1.0, 2018/11/27 15:31
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/configItemGroup")
@Slf4j
public class ConfigItemGroupApi
{
    @Autowired
    private ConfigItemGroupService configItemGroupService;
    @Autowired
    private ConfigItemService configItemService;
    @Autowired
    private ConfigItemGroupRelationService configItemGroupRelationService;

    /**
     * [简要描述]:分页获取<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemGroupDto>
     * jun.liu  2018/11/27 - 15:35
     **/
    @RequestMapping(value = "/page")
    public PageInfo<ConfigItemGroupDto> pageConfigItemGroup(@RequestBody ConfigItemGroupQuery configItemGroupQuery)
    {
        return configItemGroupService
                .pageConfigItemGroup(configItemGroupQuery, configItemGroupQuery.getPageNum(), configItemGroupQuery
                        .getPageSize());
    }

    /**
     * [简要描述]:保存<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupDto :
     * @return java.lang.Boolean
     * jun.liu  2018/11/27 - 15:44
     **/
    @RequestMapping(value = "/save")
    public Boolean save(@RequestBody ConfigItemGroupDto configItemGroupDto)
    {
        if (StringUtils.isBlank(configItemGroupDto.getGroupName()))
        {
            log.info("新增配置组失败,参数不能为空");
            throw new RuntimeException("参数不能为空");
        }
        configItemGroupDto.setCreateTime(new Date());
        int a = configItemGroupService.save(ConfigItemGroupDto.convertToEntity(configItemGroupDto));
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:获取组信息<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return com.winner.config.center.pojo.db.dto.ConfigItemGroupDto
     * jun.liu  2018/12/13 - 9:48
     **/
    @RequestMapping(value = "/select/{id}")
    public ConfigItemGroupDto getConfigItemGroupById(@PathVariable("id") Long id)
    {
        if (null == id)
        {
            log.info("获取组失败，id不能为空");
            throw new RuntimeException("参数不能为空");
        }
        return ConfigItemGroupDto.convertToDto(configItemGroupService.getConfigItemGroupById(id));
    }

    /**
     * [简要描述]:更新<br/>
     * [详细描述]:<br/>
     *
     * @param configItemGroupDto :
     * @return java.lang.Boolean
     * jun.liu  2018/11/27 - 16:08
     **/
    @RequestMapping(value = "/update")
    public Boolean update(@RequestBody ConfigItemGroupDto configItemGroupDto)
    {
        if (StringUtils.isBlank(configItemGroupDto.getGroupName()) || StringUtils
                .isBlank(configItemGroupDto.getGroupDesc()))
        {
            log.info("修改配置组失败,参数不能为空");
            throw new RuntimeException("参数不能为空");
        }
        ConfigItemGroup configItemGroup = configItemGroupService.getConfigItemGroupById(configItemGroupDto.getId());
        if (configItemGroup == null)
        {
            log.info("通过id:" + configItemGroupDto.getId() + ",获取对象失败");
            throw new RuntimeException("获取对象失败");
        }
        configItemGroup.setGroupName(configItemGroupDto.getGroupName());
        configItemGroup.setGroupDesc(configItemGroupDto.getGroupDesc());
        configItemGroup.setUpdateTime(new Date());
        int a = configItemGroupService.update(configItemGroup);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:删除<br/>
     * [详细描述]:<br/>
     *
     * @param ids :
     * @return java.lang.Boolean
     * jun.liu  2018/11/27 - 16:19
     **/
    @RequestMapping(value = "/delete/{ids}")
    public Integer delete(@PathVariable("ids") String ids)
    {
        if (StringUtils.isBlank(ids))
        {
            log.info("删除失败,id不能为空");
            throw new RuntimeException("参数不能为空");
        }
        String[] idArr = ids.split(",");
        int a = configItemGroupService.batchDelete(idArr);
        a = idArr.length - a;
        //返回0表示全部删除成功，否则返回对应失败的数量。
        return a;
    }

    /**
     * [简要描述]:获取已关联当前group的配置项<br/>
     * [详细描述]:<br/>
     *
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/27 - 16:38
     **/
    @RequestMapping(value = "/isRefGroup")
    public PageInfo<ConfigItemDto> pageRefConfigItemWithGroup(@RequestBody ConfigItemQuery configItemQuery)
    {
        if (null == configItemQuery.getGroupId())
        {
            log.info("获取已关联当前group的配置项失败，groupId不能为空");
            throw new RuntimeException("参数不能为空");
        }
        return configItemService
                .pageRefConfigItemWithGroup(configItemQuery, configItemQuery.getPageNum(), configItemQuery
                        .getPageSize());
    }

    /**
     * [简要描述]:获取未关联当前group的配置项<br/>
     * [详细描述]:<br/>
     *
     * @param configItemQuery :
     * @return com.github.pagehelper.PageInfo<com.winner.config.center.pojo.db.dto.ConfigItemDto>
     * jun.liu  2018/11/28 - 10:01
     **/
    @RequestMapping(value = "/notRefGroup")
    public PageInfo<ConfigItemDto> pageNotRefConfigItemWithGroup(@RequestBody ConfigItemQuery configItemQuery)
    {
        if (null == configItemQuery.getGroupId())
        {
            log.info("获取未关联当前group的配置项失败，groupId不能为空");
            throw new RuntimeException("参数不能为空");
        }
        return configItemService
                .pageNotRefConfigItemWithGroup(configItemQuery, configItemQuery.getPageNum(), configItemQuery
                        .getPageSize());
    }

    /**
     * [简要描述]:行政配置项和组的绑定关系<br/>
     * [详细描述]:<br/>
     *
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 9:26
     **/
    @RequestMapping(value = "/batchSave/{groupId}/{itemIds}")
    public Boolean batchSave(@PathVariable("itemIds") String itemIds, @PathVariable("groupId") Long groupId)
    {
        if (StringUtils.isBlank(itemIds) || groupId == null)
        {
            log.info("新增配置项和组绑定关系失败，itemIds和groupId不能为空");
            throw new RuntimeException("参数不能为空");
        }
        String[] itemIdArr = itemIds.split(",");
        int a = configItemGroupRelationService.batchSave(itemIdArr, groupId);
        if (a > 0)
        {
            return true;
        }
        return false;
    }

    /**
     * [简要描述]:批量删除<br/>
     * [详细描述]:<br/>
     *
     * @param itemIds :
     * @param groupId :
     * @return java.lang.Boolean
     * jun.liu  2018/11/28 - 15:12
     **/
    @RequestMapping(value = "/batchDelete/{groupId}/{itemIds}")
    public Boolean batchDelete(@PathVariable("itemIds") String itemIds, @PathVariable("groupId") Long groupId)
    {
        if (StringUtils.isBlank(itemIds) || groupId == null)
        {
            log.info("删除配置项和组绑定关系失败，itemIds和groupId不能为空");
            throw new RuntimeException("参数不能为空");
        }
        String[] itemIdArr = itemIds.split(",");
        int a = configItemGroupRelationService.batchDelete(itemIdArr, groupId);
        if (a > 0)
        {
            return true;
        }
        return false;
    }
}
