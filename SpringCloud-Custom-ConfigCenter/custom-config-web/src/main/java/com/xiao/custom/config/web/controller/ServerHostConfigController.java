package com.xiao.custom.config.web.controller;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ServerHostConfigDto;
import com.xiao.custom.config.pojo.query.ServerHostConfigQuery;
import com.xiao.custom.config.web.feign.server.ServerHostConfigFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]:服务器管理
 * [详细描述]:
 *
 * @author jyqiao
 * @version 1.0, 2018/12/20
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/api/serverHostConfig")
@Slf4j
public class ServerHostConfigController
{
    @Autowired
    private ServerHostConfigFeign serverHostConfigFeign;

    //查询服务器配置信息
    @RequestMapping(value = "/page")
    public PageInfo<ServerHostConfigDto> pageConfigItem(@RequestBody ServerHostConfigQuery serverHostConfigQuery)
    {
        return serverHostConfigFeign.pageServerHostConfig(serverHostConfigQuery);
    }

    //更改服务器配置信息
    @RequestMapping(value = "/updateServerHostConfig")
    public Boolean updateServerHostConfig(@RequestBody ServerHostConfigDto serverHostConfigDto)
    {

        return serverHostConfigFeign.updateServerHostConfig(serverHostConfigDto);
    }

    //添加服务器配置信息
    @RequestMapping(value = "/addServerHostConfig")
    public Boolean addServerHostConfig(@RequestBody ServerHostConfigDto serverHostConfigDto)
    {
        return serverHostConfigFeign.addServerHostConfig(serverHostConfigDto);
    }

    //删除服务器配置信息
    @RequestMapping(value = "/delectServerHostConfig/{id}")
    public int delectServerHostConfig(@PathVariable("id") String id)
    {
        long ids = Long.parseLong(id);
        return serverHostConfigFeign.delectServerHostConfig(ids);
    }

}
