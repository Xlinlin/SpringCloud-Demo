package com.xiao.custom.config.pojo.query;

import com.xiao.custom.config.pojo.common.BaseQuery;
import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:36
 * @since JDK 1.8
 */
@Data
public class ConfigItemQuery extends BaseQuery
{
    //配置项KEY
    private String itemKey;

    //配置项值
    private String itemValue;

    private String itemDesc;

    //0可用,1不可用
    private Integer status;

    //应用类型，0通用，1开发环境，2测试环境，3生产环境，4其他。默认通用类型
    private Integer itemType;

    private String createTime;

    private String updateTime;

    private Long groupId;
}
