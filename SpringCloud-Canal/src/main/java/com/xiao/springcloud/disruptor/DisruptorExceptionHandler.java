package com.xiao.springcloud.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import com.xiao.springcloud.disruptor.service.DisruptorService;
import lombok.extern.slf4j.Slf4j;

/**
 * [简要描述]: Disruptor自定义异常处理
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/21 15:42
 * @since JDK 1.8
 */
@Slf4j
public class DisruptorExceptionHandler implements ExceptionHandler<DataEvent>
{
    private DisruptorService disruptorService;

    public DisruptorExceptionHandler(DisruptorService disruptorService)
    {
        this.disruptorService = disruptorService;
    }

    /**
     * 事件处理异常
     */
    @Override
    public void handleEventException(Throwable throwable, long sequence, DataEvent dataEvent)
    {
        //事件处理异常里面进行补偿执行
        disruptorService.execute(dataEvent.getData());
        log.error(">>> Disruptor事件处理异常，进行立即执行补偿操作..........");
        log.error(">>> 异常信息如下：", throwable.getMessage());
    }

    /**
     * 启动异常
     *
     * @param throwable
     */
    @Override
    public void handleOnStartException(Throwable throwable)
    {
        log.error(">>> Disruptor 启动异常：{}", throwable.getMessage());
    }

    /**
     * 关闭异常
     *
     * @param throwable
     */
    @Override
    public void handleOnShutdownException(Throwable throwable)
    {
        log.error(">>> Disruptro 关闭异常：{}", throwable.getMessage());
    }
}
