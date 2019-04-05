package com.xiao.custom.config.service.service;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ServerHostConfigDto;
import com.xiao.custom.config.pojo.entity.ServerHostConfig;
import com.xiao.custom.config.pojo.query.ServerHostConfigQuery;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:45
 * @since JDK 1.8
 */
public interface ServerHostConfigService
{
    int save(ServerHostConfigDto serverHostConfigDto);

    int update(ServerHostConfigDto serverHostConfigDto);
    /**
     * [简要描述]:通过id获取<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return 
     * jyqiao
     **/
    ServerHostConfig selectServerHostConfigById(Long id);
    int delete(Long id);

    PageInfo<ServerHostConfigDto> pageServerHostConfig(ServerHostConfigQuery serverHostConfigQuery, Integer pageNum,
            Integer pageSize);
}
