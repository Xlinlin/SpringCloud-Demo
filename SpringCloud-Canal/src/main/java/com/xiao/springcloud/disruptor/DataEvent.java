package com.xiao.springcloud.disruptor;

import lombok.Data;

/**
 * [简要描述]: 定义事件
 * [详细描述]:通过 Disruptor 进行交换的数据类型
 *
 * @author llxiao
 * @version 1.0, 2019/3/21 15:12
 * @since JDK 1.8
 */
@Data
public class  DataEvent
{
    private TableData data;
}
