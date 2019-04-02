package com.xiao.custom.config.pojo.query;

import com.xiao.custom.config.pojo.common.BaseQuery;
import lombok.Data;

/**
 * [简要描述]: 配置中心客户端连接列查询条件
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/27 14:21
 * @since JDK 1.8
 */
@Data
public class ClientHostInfoQuery extends BaseQuery
{
    /**
     * IP查询
     */
    private String hostIp;

    /**
     * 状态查询
     */
    private Integer status;

    /**
     * 应用查询
     */
    private String application;
}
