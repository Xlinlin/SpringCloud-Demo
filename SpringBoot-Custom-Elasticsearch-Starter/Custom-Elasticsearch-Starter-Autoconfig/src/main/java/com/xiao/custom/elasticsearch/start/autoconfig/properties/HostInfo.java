package com.xiao.custom.elasticsearch.start.autoconfig.properties;

import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/28 10:22
 * @since JDK 1.8
 */
@Data
public class HostInfo
{
    private String hostname;
    private int port;
    private String schema;
}
