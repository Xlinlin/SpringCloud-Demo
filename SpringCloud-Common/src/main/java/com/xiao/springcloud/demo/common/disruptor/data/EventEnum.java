package com.xiao.springcloud.demo.common.disruptor.data;

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
    LOG_EVENT("log");

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
