package com.xiao.custom.config.service.service.impl;

import com.xiao.custom.config.pojo.entity.ConfigItemGroupRelation;
import com.xiao.custom.config.pojo.mapper.ConfigItemGroupRelationMapper;
import com.xiao.custom.config.service.service.ConfigItemGroupRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:43
 * @since JDK 1.8
 */
@Service
public class ConfigItemGroupRelationServiceImpl implements ConfigItemGroupRelationService
{
    @Autowired
    private ConfigItemGroupRelationMapper configItemGroupRelationMapper;

    @Override
    public Integer save(ConfigItemGroupRelation configItemGroupRelation)
    {
        return configItemGroupRelationMapper.insert(configItemGroupRelation);
    }

    @Override
    public Integer update(ConfigItemGroupRelation configItemGroupRelation)
    {
        return configItemGroupRelationMapper.updateByPrimaryKey(configItemGroupRelation);
    }

    @Override
    public void delete(Long id)
    {
        configItemGroupRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchSave(String[] itemIdArr, Long groupId)
    {
        return configItemGroupRelationMapper.batchSave(itemIdArr, groupId);
    }

    @Override
    public int batchDelete(String[] itemIdArr, Long groupId)
    {
        return configItemGroupRelationMapper.batchDelete(itemIdArr, groupId);
    }
}
