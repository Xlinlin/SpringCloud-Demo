package com.xiao.skywalking.demo.common.disruptor.service.impl;

import com.purcotton.omni.common.disruptor.data.BasisData;
import com.purcotton.omni.common.disruptor.event.ServiceEvent;
import com.purcotton.omni.common.disruptor.service.DisruptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * [简要描述]: 异步处理日志
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2019-04-24 15:31
 * @since JDK 1.8
 */
@Service
public class DisruptorServiceImpl implements DisruptorService
{
    @Autowired
    ApplicationContext applicationContext;

    /**
     * [简要描述]: 具体业务处理
     * [详细描述]:<br/>
     *
     * @param basisData : 待处理数据
     * mjye  2019-04-24 - 15:32
     **/
    @Override
    public void execute(BasisData basisData)
    {
        ServiceEvent event = new ServiceEvent(basisData, basisData.getEvent());
        applicationContext.publishEvent(event);
    }

}