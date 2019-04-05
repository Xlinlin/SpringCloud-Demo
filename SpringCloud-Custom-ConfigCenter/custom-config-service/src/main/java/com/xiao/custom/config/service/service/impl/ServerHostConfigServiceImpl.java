package com.xiao.custom.config.service.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ServerHostConfigDto;
import com.xiao.custom.config.pojo.entity.ServerHostConfig;
import com.xiao.custom.config.pojo.mapper.ApplicationMapper;
import com.xiao.custom.config.pojo.mapper.ServerHostConfigMapper;
import com.xiao.custom.config.pojo.query.ServerHostConfigQuery;
import com.xiao.custom.config.service.service.ServerHostConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:48
 * @since JDK 1.8
 */
@Service
public class ServerHostConfigServiceImpl implements ServerHostConfigService
{
    @Resource
    private ServerHostConfigMapper serverHostConfigMapper;

    @Autowired
    private ApplicationMapper applicationConfigMapper;

    @Override
    @Transactional
    public int save(ServerHostConfigDto serverHostConfigDto)
    {
        serverHostConfigDto.setCreateTime(new Date());
        ServerHostConfig serverHostConfig = serverHostConfigDtoconvertserverHostConfig(serverHostConfigDto);
        return serverHostConfigMapper.insert(serverHostConfig);
    }

    @Override
    @Transactional
    public int update(ServerHostConfigDto serverHostConfigDto)
    {
        ServerHostConfig serverHostConfig = serverHostConfigDtoconvertserverHostConfig(serverHostConfigDto);
        return serverHostConfigMapper.updateByPrimaryKey(serverHostConfig);
    }

    /**
     * [简要描述]: -1 标识删除失败，已关联应用不能删除该区域，必须先删除应用<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return int
     * llxiao  2019/1/2 - 17:54
     **/
    @Override
    @Transactional
    public int delete(Long id)
    {
        //        if (applicationConfigMapper.countByRegionId(id) > 0)
        //        {
        //            return -1;
        //        }
        return serverHostConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ServerHostConfigDto> pageServerHostConfig(ServerHostConfigQuery serverHostConfigQuery,
            Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<ServerHostConfigDto> list = serverHostConfigMapper.pageServerHostConfig(serverHostConfigQuery);
        return new PageInfo<>(list);
    }

    /**
     * [简要描述]:serverHostConfigDto转ServerHostConfig<br/>
     * [详细描述]:serverHostConfigDto转ServerHostConfig<br/>
     *
     * @return ServerHostConfig
     **/
    public ServerHostConfig serverHostConfigDtoconvertserverHostConfig(ServerHostConfigDto serverHostConfigDto)
    {
        ServerHostConfig serverHostConfig = new ServerHostConfig();
        serverHostConfig.setId(serverHostConfigDto.getId());
        serverHostConfig.setRegionId(serverHostConfigDto.getRegionId());
        serverHostConfig.setServerDesc(serverHostConfigDto.getServerDesc());
        serverHostConfig.setServerHost(serverHostConfigDto.getServerHost());
        serverHostConfig.setCreateTime(serverHostConfigDto.getCreateTime());
        serverHostConfig.setUpdateTime(new Date());

        return serverHostConfig;
    }

    @Override
    public ServerHostConfig selectServerHostConfigById(Long id)
    {
        // TODO Auto-generated method stub
        return serverHostConfigMapper.selectByPrimaryKey(id);
    }
}
