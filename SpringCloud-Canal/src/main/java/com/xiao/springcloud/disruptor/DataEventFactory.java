package com.xiao.springcloud.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * [简要描述]: 定义事件工厂
 * [详细描述]: 定义了如何实例化定义的事件(Event)
 * Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例<p>
 * 一个 Event 实例实际上被用作一个“数据槽”，发布者发布前，先从 RingBuffer 获得一个 Event 的实例，然后往 Event 实例中填充数据，<br>
 * 之后再发布到 RingBuffer 中，之后由 Consumer 获得该 Event 实例并从中读取数据<br>
 *
 * @author llxiao
 * @version 1.0, 2019/3/21 15:17
 * @since JDK 1.8
 */
public class DataEventFactory implements EventFactory<DataEvent>
{
    @Override
    public DataEvent newInstance()
    {
        return new DataEvent();
    }
}
