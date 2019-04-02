package com.xiao.custom.config.service.service;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ClientHostInfoDto;
import com.xiao.custom.config.pojo.query.ClientHostInfoQuery;

/**
 * [简要描述]: 客户端连接服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/27 14:13
 * @since JDK 1.8
 */
public interface ClientHostService
{
    /**
     * 分页查询客户端信息
     *
     * @param query
     * @return
     */
    PageInfo<ClientHostInfoDto> pageQuery(ClientHostInfoQuery query);

    /**
     * 删除客户端连接信息
     *
     * @param id
     */
    void delete(long id);
}
