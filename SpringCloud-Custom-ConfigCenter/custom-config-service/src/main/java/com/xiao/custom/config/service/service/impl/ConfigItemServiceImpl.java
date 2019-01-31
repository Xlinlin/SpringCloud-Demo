package com.xiao.custom.config.service.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemDto;
import com.xiao.custom.config.pojo.entity.ConfigItem;
import com.xiao.custom.config.pojo.mapper.ConfigItemGroupRelationMapper;
import com.xiao.custom.config.pojo.mapper.ConfigItemMapper;
import com.xiao.custom.config.pojo.query.ConfigItemQuery;
import com.xiao.custom.config.service.service.ConfigItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:38
 * @since JDK 1.8
 */
@Service
public class ConfigItemServiceImpl implements ConfigItemService
{
    @Autowired
    private ConfigItemMapper configItemMapper;
    @Autowired
    private ConfigItemGroupRelationMapper configItemGroupRelationMapper;

    @Override
    public Integer save(ConfigItem configItem)
    {
        return configItemMapper.insert(configItem);
    }

    @Override
    public Integer update(ConfigItem configItem)
    {
        return configItemMapper.updateByPrimaryKey(configItem);
    }

    @Override
    public void delete(Long id)
    {
        configItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ConfigItemDto> pageConfigItem(ConfigItemQuery configItemQuery, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ConfigItemDto> list = configItemMapper.pageConfigItem(configItemQuery);
        return new PageInfo<>(list);
    }

    @Override
    public ConfigItem getConfigItemById(Long id)
    {
        return configItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<ConfigItemDto> pageRefConfigItemWithGroup(ConfigItemQuery configItemQuery, int pageNum,
            int pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ConfigItemDto> list = configItemMapper.pageRefConfigItemWithGroup(configItemQuery);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ConfigItemDto> pageNotRefConfigItemWithGroup(ConfigItemQuery configItemQuery, int pageNum,
            int pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ConfigItemDto> list = configItemMapper.pageNotRefConfigItemWithGroup(configItemQuery);
        return new PageInfo<>(list);
    }

    @Override
    public int batchDelete(String[] idArr)
    {
        List<String> dels = new ArrayList<>();
        //已经关联组的配置项，不能进行删除，必须先解除组与配置项的关系才能进行删除
        for (String itemId : idArr)
        {
            if (configItemGroupRelationMapper.countByItemId(itemId) > 0)
            {
                continue;
            }
            else
            {
                dels.add(itemId);
            }
        }
        //        configItemGroupRelationMapper.batchDeleteByItemId(idArr);
        if (CollectionUtil.isNotEmpty(dels))
        {
            return configItemMapper.batchDelete(ArrayUtil.toArray(dels, String.class));
        }
        else
        {
            return 0;
        }
    }
}
