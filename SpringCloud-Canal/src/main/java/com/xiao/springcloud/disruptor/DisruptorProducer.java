package com.xiao.springcloud.disruptor;

import cn.hutool.core.collection.CollectionUtil;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.xiao.springcloud.disruptor.service.DisruptorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * [简要描述]: Disruptor生产者
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/21 15:37
 * @since JDK 1.8
 */
@Component
@Slf4j
public class DisruptorProducer implements DisposableBean, ApplicationListener<ContextRefreshedEvent>
{
    /**
     * RingBuffer 大小，必须是 2 的 N 次方；
     */
    private int ringBufferSize = 1024 * 1024;

    private Disruptor<DataEvent> disruptor;

    private boolean isMultiProducer;

    /**
     * 等待策略
     * 例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
     * BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
     * SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
     * YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
     */
    private WaitStrategy waitStrategy;

    private RingBuffer<DataEvent> ringBuffer;

    private volatile boolean isStart;

    private DisruptorService disruptorService;

    private ExecutorService executor;
    private int threads;

    @Autowired
    public DisruptorProducer(DisruptorService disruptorService)
    {

        threads = Runtime.getRuntime().availableProcessors();
        this.disruptorService = disruptorService;

        //单生产者
        isMultiProducer = true;
        ProducerType producerType = ProducerType.SINGLE;
        if (isMultiProducer)
        {
            //多生产者
            producerType = ProducerType.MULTI;
        }
        // 等待策略
        waitStrategy = new YieldingWaitStrategy();

        //初始化disruptor
        disruptor = new Disruptor<>(new DataEventFactory(), ringBufferSize, DisruptorThreadFactory
                .create("Disruptor Main-", false), producerType, waitStrategy);

        executor = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), DisruptorThreadFactory
                .create("Disruptor Producer-", false), new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * [简要描述]:发送事件处理<br/>
     * [详细描述]:<br/>
     *
     * @param dataList : 待处理的数据
     * llxiao  2019/3/25 - 15:10
     **/
    public void send(List<TableData> dataList)
    {
        if (log.isDebugEnabled())
        {
            log.debug("批量发送数据给消费者，总数量：{}", CollectionUtil.isNotEmpty(dataList) ? 0 : dataList.size());
        }
        if (CollectionUtil.isNotEmpty(dataList))
        {
            executor.execute(() ->
            {
                for (TableData tableData : dataList)
                {
                    translator(tableData);
                }
            });
        }
    }

    private void translator(TableData tableData)
    {
        EventTranslatorOneArg<DataEvent, TableData> eventTranslatorOneArg = (event, sequence, message1) -> event
                .setData(tableData);
        ringBuffer.publishEvent(eventTranslatorOneArg, tableData);
    }

    @Override
    public void destroy() throws Exception
    {
        if (isStart)
        {
            //关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
            disruptor.shutdown();
        }
        executor.shutdown();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
    {
        log.info(">>> Disruptor producer 启动......");
        doStart();
    }

    private void doStart()
    {
        //CPU可使用数量
        final Executor executor = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), DisruptorThreadFactory
                .create("Disruptor consumer-", false), new ThreadPoolExecutor.AbortPolicy());
        DisruptorConsumer[] disruptorConsumers = new DisruptorConsumer[threads];
        for (int i = 0; i < threads; i++)
        {
            disruptorConsumers[i] = new DisruptorConsumer(disruptorService, executor);
        }
        //设置消费者Handler
        // 广播 消费者之间无依赖关系
        //        disruptor.handleEventsWith(ArrayUtil.toArray(handlers, EventHandler.class));
        // 关闭 消费间有依赖关系,假设：2号需要在0号和1号处理完才能进行
        //        disruptor.handleEventsWith(handlers.get(0),handlers.get(1)).then(handlers.get(2));
        // 分组  List<WorkHandler> workHandlers
        disruptor.handleEventsWithWorkerPool(disruptorConsumers);

        //设置异常处理
        disruptor.setDefaultExceptionHandler(new DisruptorExceptionHandler(disruptorService));

        ringBuffer = disruptor.start();
        this.isStart = true;
    }

}
