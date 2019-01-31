package com.xiao.custom.config.service.api;

import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ServerHostConfigDto;
import com.xiao.custom.config.pojo.entity.ServerHostConfig;
import com.xiao.custom.config.pojo.query.ServerHostConfigQuery;
import com.xiao.custom.config.service.service.ServerHostConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [简要描述]:服务管理
 * [详细描述]:服务管理
 *
 * @author jyqiao
 * @version 1.0, 2018年11月27日
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/serverHostConfig")
@Slf4j
public class ServerHostConfigApi
{

    @Autowired
    private ServerHostConfigService serverHostConfigService;

    //分页查询
    @RequestMapping(value = "/queryServerHost")
    public PageInfo<ServerHostConfigDto> queryServerHost(@RequestBody ServerHostConfigQuery serverHostConfigQuery)
    {

        return serverHostConfigService
                .pageServerHostConfig(serverHostConfigQuery, serverHostConfigQuery.getPageNum(), serverHostConfigQuery
                        .getPageSize());
    }

    //删除服务管理
    @RequestMapping(value = "/delectServerHostConfig/{id}")
    public int delectServerHostConfig(@PathVariable("id") Long id)
    {
        if (null == id)
        {
            log.info("删除服务管理失败，id不能为空");
            throw new RuntimeException("参数不能为空");
        }
        return serverHostConfigService.delete(id);
    }

    //更改服务管理
    @PostMapping(value = "/updateServerHostConfig")
    public Boolean updateServerHostConfig(@RequestBody ServerHostConfigDto serverHostConfigDto)
    {
        if (null == serverHostConfigDto.getId() || StringUtils.isBlank(serverHostConfigDto.getServerDesc())
                || StringUtils.isBlank(serverHostConfigDto.getServerHost()) || null == serverHostConfigDto
                .getRegionId())
        {
            log.info("更改服务管理失败,参数不能为空");
            throw new RuntimeException("参数不能为空");

        }
        int b = serverHostConfigService.update(serverHostConfigDto);

        if (b > 0)
        {
            return true;
        }
        return false;
    }

    //添加服务管理
    @PostMapping(value = "/addServerHostConfig")
    public Boolean addServerHostConfig(@RequestBody ServerHostConfigDto serverHostConfigDto)
    {
        if (StringUtils.isBlank(serverHostConfigDto.getServerDesc()) || StringUtils
                .isBlank(serverHostConfigDto.getServerHost()) || null == serverHostConfigDto.getRegionId())
        {
            log.info("新增服务管理失败,参数不能为空");
            throw new RuntimeException("参数不能为空");

        }
        int b = serverHostConfigService.save(serverHostConfigDto);
        if (b > 0)
        {
            return true;
        }
        return false;

    }

    @RequestMapping(value = "/selectServerHostConfigById/{id}")
    public ServerHostConfigDto selectServerHostConfigById(@PathVariable("id") Long id)
    {
        if (null == id)
        {
            log.info("获取服务管理失败，id不能为空");
            throw new RuntimeException("参数不能为空");
        }

        return serverHostConfigToDto(serverHostConfigService.selectServerHostConfigById(id));

    }

    //po转dto
    public ServerHostConfigDto serverHostConfigToDto(ServerHostConfig serverHostConfig)
    {
        ServerHostConfigDto serverHostConfigDto = new ServerHostConfigDto();
        serverHostConfigDto.setId(serverHostConfig.getId());
        serverHostConfigDto.setRegionId(serverHostConfig.getRegionId());
        serverHostConfigDto.setCreateTime(serverHostConfig.getCreateTime());
        serverHostConfigDto.setServerDesc(serverHostConfig.getServerDesc());
        serverHostConfigDto.setServerHost(serverHostConfig.getServerHost());
        serverHostConfigDto.setUpdateTime(serverHostConfig.getUpdateTime());

        return serverHostConfigDto;
    }

}
