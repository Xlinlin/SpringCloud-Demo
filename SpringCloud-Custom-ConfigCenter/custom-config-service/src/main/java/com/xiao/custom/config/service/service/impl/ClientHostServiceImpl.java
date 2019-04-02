package com.xiao.custom.config.service.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ClientHostInfoDto;
import com.xiao.custom.config.pojo.mapper.ClientHostInfoMapper;
import com.xiao.custom.config.pojo.query.ClientHostInfoQuery;
import com.xiao.custom.config.service.service.ClientHostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/27 15:24
 * @since JDK 1.8
 */
@Service
public class ClientHostServiceImpl implements ClientHostService
{
    @Autowired
    private ClientHostInfoMapper clientHostInfoMapper;

    /**
     * 分页查询客户端信息
     *
     * @param query
     * @return
     */
    @Override
    public PageInfo<ClientHostInfoDto> pageQuery(ClientHostInfoQuery query)
    {
        List<ClientHostInfoDto> clientHostInfoDtos = new ArrayList<>();
        if (null != query)
        {
            PageHelper.startPage(query.getPageNum(), query.getPageSize());
            clientHostInfoDtos = this.clientHostInfoMapper.pageQuery(query);
        }
        return new PageInfo<>(clientHostInfoDtos);
    }

    /**
     * 删除客户端连接信息
     *
     * @param id
     */
    @Override
    public void delete(long id)
    {
        this.clientHostInfoMapper.deleteByPrimaryKey(id);
    }
}
