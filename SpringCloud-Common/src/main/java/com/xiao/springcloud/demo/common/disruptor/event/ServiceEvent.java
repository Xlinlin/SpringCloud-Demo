package com.xiao.springcloud.demo.common.disruptor.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

/**
 * [简要描述]: Springboot 的Event数据事件
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/6/4 08:39
 * @since JDK 1.8
 */
@Setter
@Getter
public class ServiceEvent extends ApplicationEvent
{
    /**
     * 事件类型
     */
    private String event;

    /**
     * Create a new ApplicationEvent.
     */
    public ServiceEvent(Object source, String event)
    {
        super(source);
        this.event = event;
    }
}
