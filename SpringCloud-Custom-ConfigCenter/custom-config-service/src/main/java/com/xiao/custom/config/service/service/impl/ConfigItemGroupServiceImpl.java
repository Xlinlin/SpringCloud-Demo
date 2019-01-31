package com.xiao.custom.config.service.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ConfigItemGroupDto;
import com.xiao.custom.config.pojo.entity.ConfigItemGroup;
import com.xiao.custom.config.pojo.mapper.ApplicationItemGroupRelationMapper;
import com.xiao.custom.config.pojo.mapper.ConfigItemGroupMapper;
import com.xiao.custom.config.pojo.mapper.ConfigItemGroupRelationMapper;
import com.xiao.custom.config.pojo.query.ConfigItemGroupQuery;
import com.xiao.custom.config.service.service.ConfigItemGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 17:54
 * @since JDK 1.8
 */
@Service
public class ConfigItemGroupServiceImpl implements ConfigItemGroupService
{
    @Autowired
    private ConfigItemGroupMapper configItemGroupMapper;
    @Autowired
    private ConfigItemGroupRelationMapper configItemGroupRelationMapper;

    @Autowired
    private ApplicationItemGroupRelationMapper applicationItemGroupRelationMapper;

    @Override
    public Integer save(ConfigItemGroup configItemGroup)
    {
        return configItemGroupMapper.insert(configItemGroup);
    }

    @Override
    public Integer update(ConfigItemGroup configItemGroup)
    {
        return configItemGroupMapper.updateByPrimaryKey(configItemGroup);
    }

    @Override
    public Integer delete(Long id)
    {
        return configItemGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ConfigItemGroupDto> pageConfigItemGroup(ConfigItemGroupQuery configItemGroupQuery, Integer pageNum,
            Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ConfigItemGroupDto> list = configItemGroupMapper.pageConfigItemGroup(configItemGroupQuery);
        return new PageInfo<>(list);
    }

    @Override
    public ConfigItemGroup getConfigItemGroupById(Long id)
    {
        return configItemGroupMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo<ConfigItemGroupDto> pageRefGroupWithApp(ConfigItemGroupQuery configItemGroupQuery, int pageNum,
            int pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ConfigItemGroupDto> list = configItemGroupMapper.pageRefGroupWithApp(configItemGroupQuery);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<ConfigItemGroupDto> pageNotRefGroupWithApp(ConfigItemGroupQuery configItemGroupQuery, int pageNum,
            int pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ConfigItemGroupDto> list = configItemGroupMapper.pageNotRefGroupWithApp(configItemGroupQuery);
        return new PageInfo<>(list);
    }

    @Override
    public int batchDelete(String[] idArr)
    {
        List<String> delGroupIds = new ArrayList<>();
        //删除组之前，已经关联应用的不能进行删除，必须先从应用中解除
        for (String groupId : idArr)
        {
            //删除组之前，已经关联应用的不能进行删除，必须先从应用中解除
            if (applicationItemGroupRelationMapper.countByGroupId(groupId) > 0)
            {
                continue;
            }
            else
            {
                delGroupIds.add(groupId);
            }
        }
        String[] del = ArrayUtil.toArray(delGroupIds, String.class);
        // 删除组关联的配置项
        configItemGroupRelationMapper.batchDeleteByGroupId(del);
        return configItemGroupMapper.batchDelete(del);
    }
}
