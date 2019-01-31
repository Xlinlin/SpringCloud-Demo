package com.xiao.custom.config.service.service.impl;

import com.xiao.custom.config.pojo.entity.ApplicationItemGroupRelation;
import com.xiao.custom.config.pojo.mapper.ApplicationItemGroupRelationMapper;
import com.xiao.custom.config.service.service.ApplicationItemGroupRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:03
 * @since JDK 1.8
 */
@Service
public class ApplicationItemGroupRelationServiceImpl implements ApplicationItemGroupRelationService
{
    @Autowired
    private ApplicationItemGroupRelationMapper applicationItemGroupRelationMapper;

    @Override
    public Integer save(ApplicationItemGroupRelation applicationItemGroupRelation)
    {
        return applicationItemGroupRelationMapper.insert(applicationItemGroupRelation);
    }

    @Override
    public Integer update(ApplicationItemGroupRelation applicationItemGroupRelation)
    {
        return applicationItemGroupRelationMapper.updateByPrimaryKey(applicationItemGroupRelation);
    }

    @Override
    public void delete(Long id)
    {
        applicationItemGroupRelationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int batchSave(String[] groupIdArr, Long appId)
    {
        return applicationItemGroupRelationMapper.batchSave(groupIdArr, appId);
    }

    @Override
    public int batchDelete(String[] groupIdArr, Long appId)
    {
        return applicationItemGroupRelationMapper.batchDelete(groupIdArr, appId);
    }
}
