package com.xiao.custom.config.pojo.query;

import com.xiao.custom.config.pojo.common.BaseQuery;
import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/27 09:47
 * @since JDK 1.8
 */
@Data
public class ServerHostConfigQuery extends BaseQuery
{
    //IP地址
    private String serverHost;

    //服务描述
    private String serverDesc;

    //开始时间
    private String createTime;
    //结束时间
    private String updateTime;
}
