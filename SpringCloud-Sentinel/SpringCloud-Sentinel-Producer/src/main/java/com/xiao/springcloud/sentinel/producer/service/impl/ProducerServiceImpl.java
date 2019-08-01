package com.xiao.springcloud.sentinel.producer.service.impl;

import com.xiao.springcloud.sentinel.producer.service.ProducerService;
import org.springframework.stereotype.Service;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 09:57
 * @since JDK 1.8
 */
@Service
public class ProducerServiceImpl implements ProducerService
{
    /**
     * [简要描述]:超时APi模拟<br/>
     * [详细描述]:<br/>
     *
     * @param input :
     * @return java.lang.String
     * llxiao  2019/8/1 - 9:57
     **/
    @Override
    public String timeout(String input)
    {
        try
        {
            // 模拟超时2S钟
            Thread.sleep(4000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return "producer timeout: " + input;
    }

    /**
     * [简要描述]:正常APi<br/>
     * [详细描述]:<br/>
     *
     * @param input :
     * @return java.lang.String
     * llxiao  2019/8/1 - 9:59
     **/
    @Override
    public String normal(String input)
    {
        return "producer normal: " + input;
    }
}
