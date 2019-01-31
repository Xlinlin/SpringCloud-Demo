package com.xiao.custom.config.pojo.entity;

import lombok.Data;

import java.util.List;

/**
 * [简要描述]: 配置中心连接的应用客户端信息
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/29 14:37
 * @since JDK 1.8
 */
@Data
public class ClientInfo
{
    private String applicationName;
    private List<ClientHostInfo> hostInofs;
}
