package com.xiao.custom.config.pojo.dto;

import com.xiao.custom.config.pojo.entity.ConfigItemGroup;
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
public class ConfigItemGroupDto
{
    //
    private Long id;

    //组名称
    private String groupName;

    //组描述
    private String groupDesc;

    //
    private Date createTime;

    //
    private Date updateTime;

    public static ConfigItemGroup convertToEntity(ConfigItemGroupDto configItemGroupDto)
    {
        ConfigItemGroup configItemGroup = null;
        if (configItemGroupDto != null)
        {
            configItemGroup = new ConfigItemGroup();
            configItemGroup.setCreateTime(configItemGroupDto.getCreateTime());
            configItemGroup.setGroupDesc(configItemGroupDto.getGroupDesc());
            configItemGroup.setGroupName(configItemGroupDto.getGroupName());
            configItemGroup.setId(configItemGroupDto.getId());
            configItemGroup.setUpdateTime(configItemGroupDto.getUpdateTime());
        }
        return configItemGroup;
    }

    public static ConfigItemGroupDto convertToDto(ConfigItemGroup configItemGroup)
    {
        ConfigItemGroupDto dto = null;
        if (configItemGroup != null)
        {
            dto = new ConfigItemGroupDto();
            dto.setCreateTime(configItemGroup.getCreateTime());
            dto.setGroupDesc(configItemGroup.getGroupDesc());
            dto.setGroupName(configItemGroup.getGroupName());
            dto.setId(configItemGroup.getId());
            dto.setUpdateTime(configItemGroup.getUpdateTime());
        }
        return dto;
    }
}