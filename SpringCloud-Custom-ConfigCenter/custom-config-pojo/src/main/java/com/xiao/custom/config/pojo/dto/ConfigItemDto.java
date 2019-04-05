package com.xiao.custom.config.pojo.dto;

import com.xiao.custom.config.pojo.entity.ConfigItem;
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
public class ConfigItemDto
{
    //
    private Long id;

    //配置项KEY
    private String itemKey;

    //配置项值
    private String itemValue;

    //配置项描述
    private String itemDesc;

    //
    private Date createTime;

    //
    private Date updateTime;

    //0可用,1不可用
    private Integer status;

    //应用类型，0通用，1开发环境，2测试环境，3生产环境，4其他。默认通用类型
    private Integer itemType;

    public static ConfigItem convertToEntity(ConfigItemDto configItemDto)
    {
        ConfigItem configItem = null;
        if (configItemDto != null)
        {
            configItem = new ConfigItem();
            configItem.setCreateTime(configItemDto.getCreateTime());
            configItem.setItemDesc(configItemDto.getItemDesc());
            configItem.setItemKey(configItemDto.getItemKey());
            configItem.setItemValue(configItemDto.getItemValue());
            configItem.setStatus(configItemDto.getStatus());
            configItem.setId(configItemDto.getId());
            configItem.setItemType(configItemDto.getItemType());
            configItem.setUpdateTime(configItemDto.getUpdateTime());
        }
        return configItem;
    }

    public static ConfigItemDto convertToDto(ConfigItem configItem)
    {
        ConfigItemDto configItemDto = null;
        if (configItem != null)
        {
            configItemDto = new ConfigItemDto();
            configItemDto.setCreateTime(configItem.getCreateTime());
            configItemDto.setId(configItem.getId());
            configItemDto.setItemDesc(configItem.getItemDesc());
            configItemDto.setItemKey(configItem.getItemKey());
            configItemDto.setItemType(configItem.getItemType());
            configItemDto.setItemValue(configItem.getItemValue());
            configItemDto.setStatus(configItem.getStatus());
            configItemDto.setUpdateTime(configItem.getUpdateTime());
        }
        return configItemDto;
    }
}