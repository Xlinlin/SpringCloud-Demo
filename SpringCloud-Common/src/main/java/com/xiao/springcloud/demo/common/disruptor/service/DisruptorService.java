package com.xiao.springcloud.demo.common.disruptor.service;

import com.xiao.springcloud.demo.common.disruptor.data.BasisData;

/**
 * [简要描述]: 异步队列服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/25 09:50
 * @since JDK 1.8
 */
public interface DisruptorService
{
    /**
     * [简要描述]:异步处理服务<br/>
     * [详细描述]:<br/>
     *
     * @param data : 数据处理
     * llxiao  2019/3/25 - 9:51
     **/
    void execute(BasisData data);
}
