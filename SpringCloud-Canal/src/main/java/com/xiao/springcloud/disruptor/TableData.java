package com.xiao.springcloud.disruptor;

import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/25 09:48
 * @since JDK 1.8
 */
@Data
public class TableData
{
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 表名
     */
    private String tableName;
}
