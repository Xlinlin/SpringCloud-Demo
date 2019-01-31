package com.xiao.custom.config.pojo.query;

import com.xiao.custom.config.pojo.common.BaseQuery;
import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 17:53
 * @since JDK 1.8
 */
@Data
public class ConfigItemGroupQuery extends BaseQuery
{
    //组名称
    private String groupName;

    private String groupDesc;

    private String createTime;

    private String updateTime;

    private Long appId;
}
