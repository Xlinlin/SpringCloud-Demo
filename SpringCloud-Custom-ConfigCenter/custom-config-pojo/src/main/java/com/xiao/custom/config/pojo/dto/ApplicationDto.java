package com.xiao.custom.config.pojo.dto;

import com.xiao.custom.config.pojo.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* Created by Mybatis Generator on 2018/11/23
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto
{
    //
    private Long id;

    //应用名称
    private String application;

    //应用描述
    private String applicationName;

    //
    private String label;

    //环境
    private String profile;

    //
    private Date createTime;

    //
    private Date updateTime;

    //所属区域
    private Long regionId;

    //区域名称
    private String regionName;

    private String groupIds;

    public static Application convertToEntity(ApplicationDto applicationConfigDto)
    {
        Application applicationConfig = null;
        if (applicationConfigDto != null)
        {
            applicationConfig = new Application();
            applicationConfig.setApplicationName(applicationConfigDto.getApplicationName());
            applicationConfig.setCreateTime(applicationConfigDto.getCreateTime());
            applicationConfig.setLabel(applicationConfigDto.getLabel());
            applicationConfig.setProfile(applicationConfigDto.getProfile());
            applicationConfig.setRegionId(applicationConfigDto.getRegionId());
            applicationConfig.setId(applicationConfigDto.getId());
            applicationConfig.setUpdateTime(applicationConfigDto.getUpdateTime());
            applicationConfig.setApplication(applicationConfigDto.getApplication());
        }
        return applicationConfig;
    }
}