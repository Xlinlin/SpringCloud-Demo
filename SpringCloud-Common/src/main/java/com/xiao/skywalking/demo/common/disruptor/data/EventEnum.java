package com.xiao.skywalking.demo.common.disruptor.data;

/**
 * [简要描述]: 事件类型
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/6/4 08:43
 * @since JDK 1.8
 */
public enum EventEnum
{
    /**
     * 日志事件
     */
    LOG_EVENT("log"),
    /**
     * 平台商品处理事件
     */
    PLATFORM_COMMODITY("platform");

    private String event;

    EventEnum(String event)
    {
        this.event = event;
    }

    public String getEvent()
    {
        return event;
    }
}
