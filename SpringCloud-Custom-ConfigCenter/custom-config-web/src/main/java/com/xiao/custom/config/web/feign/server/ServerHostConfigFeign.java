package com.xiao.custom.config.web.feign.server;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ServerHostConfigDto;
import com.xiao.custom.config.pojo.query.ServerHostConfigQuery;
import com.xiao.custom.config.web.commo.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jyqiao
 * @version 1.0, 2018/12/20 13:58
 * @since JDK 1.8
 */
@FeignClient(value = Constants.CONFIG_SERVICE)
@RequestMapping(value = "/serverHostConfig")
public interface ServerHostConfigFeign
{
    //查询服务器配置信息
    @RequestMapping(value = "/queryServerHost")
    PageInfo<ServerHostConfigDto> pageServerHostConfig(@RequestBody ServerHostConfigQuery serverHostConfigQuery);

    //添加服务器配置信息
    @RequestMapping(value = "/addServerHostConfig")
    Boolean addServerHostConfig(@RequestBody ServerHostConfigDto serverHostConfigDto);

    //更改服务器配置信息
    @RequestMapping(value = "/updateServerHostConfig")
    Boolean updateServerHostConfig(@RequestBody ServerHostConfigDto serverHostConfigDto);

    //删除服务器配置信息
    @RequestMapping(value = "/delectServerHostConfig/{id}")
    int delectServerHostConfig(@PathVariable("id") Long id);

}
