package com.xiao.hystrix.demo.producer.service;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/8/1 09:56
 * @since JDK 1.8
 */
public interface ProducerService
{
    /**
     * [简要描述]:超时APi模拟<br/>
     * [详细描述]:<br/>
     *
     * @param input :
     * @return java.lang.String
     * llxiao  2019/8/1 - 9:57
     **/
    String timeout(String input);

    /**
     * [简要描述]:正常APi<br/>
     * [详细描述]:<br/>
     *
     * @param input :
     * @return java.lang.String
     * llxiao  2019/8/1 - 9:59
     **/
    String normal(String input);
}
