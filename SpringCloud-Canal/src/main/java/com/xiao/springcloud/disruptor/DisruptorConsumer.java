package com.xiao.springcloud.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.xiao.springcloud.disruptor.service.DisruptorService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;

/**
 * [简要描述]: Disruptor消费者之Handler。
 * [详细描述]:
 * <br>
 * 单消费者：实现EventHandler接口(DataEvent dataEvent, long sequence, boolean endOfBatch)方法<br>
 * 多消费者：实现WorkHandler接口的onEvent(DataEvent dataEvent)方法<br>
 * --广播：对于多个消费者，每条信息会达到所有的消费者，被多次处理，一般每个消费者业务逻辑不通，用于同一个消息的不同业务逻辑处理<br>
 * --消费者之间无依赖关系 disruptor.handleEventsWith(handler1,handler2,handler3);<br>
 * --假设handler3必须在handler1，handler2处理完成后进行处理:disruptor.handleEventsWith(handler1,handler2).then(handler3);<br>
 * --分组：对于同一组内的多个消费者，每条信息只会被组内一个消费者处理，每个消费者业务逻辑一般相同，用于多消费者并发处理一组消息<br>
 * --假设handler1，handler2，handler3都实现了WorkHandler，则调用以下代码就可以实现分组：<br>
 * --disruptor.handleEventsWithWorkerPool(handler1, handler2, handler3);<br>
 * 广播和分组之间也是可以排列组合的<br>
 * link：http://www.importnew.com/27652.html
 *
 * @author llxiao
 * @version 1.0, 2019/3/21 15:20
 * @since JDK 1.8
 */
@Slf4j
public class DisruptorConsumer implements EventHandler<DataEvent>, WorkHandler<DataEvent>
{
    //具体的服务
    private DisruptorService disruptorService;

    /**
     *
     */
    private final Executor executor;

    public DisruptorConsumer(DisruptorService disruptorService, Executor executor)
    {
        this.disruptorService = disruptorService;
        this.executor = executor;
    }

    @Override
    public void onEvent(DataEvent dataEvent) throws Exception
    {
        if (log.isDebugEnabled())
        {
            log.info("接受到数据更新请求  >>>" + dataEvent);
        }
        executor.execute(() ->
        {
            disruptorService.execute(dataEvent.getData());
        });

    }

    @Override
    public void onEvent(DataEvent dataEvent, long sequence, boolean endOfBatch) throws Exception
    {
        if (log.isDebugEnabled())
        {
            log.info("接受到数据更新请求  >>>{}", dataEvent);
            log.info("Sequence:{}", sequence);
            log.info("End Of Batch：{}", endOfBatch);
        }
        this.onEvent(dataEvent);
    }
}
