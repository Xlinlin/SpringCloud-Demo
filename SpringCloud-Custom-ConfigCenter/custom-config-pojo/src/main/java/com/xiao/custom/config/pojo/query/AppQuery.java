package com.xiao.custom.config.pojo.query;

import com.xiao.custom.config.pojo.common.BaseQuery;
import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 16:51
 * @since JDK 1.8
 */
@Data
public class AppQuery extends BaseQuery
{
    //应用
    private String application;

    //应用描述
    private String applicationName;

    //环境
    private String profile;

    //创建开始时间
    private Data startTime;
    //创建结束时间
    private Data endTime;
}
