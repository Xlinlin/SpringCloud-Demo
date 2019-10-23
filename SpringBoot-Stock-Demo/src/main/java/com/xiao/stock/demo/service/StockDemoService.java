package com.xiao.stock.demo.service;

import com.xiao.stock.demo.entity.OrderDemo;
import com.xiao.stock.demo.entity.StockDemo;

/**
 * [简要描述]: 库存操作服务
 * [详细描述]:
 * 1.使用redis 分布式锁
 * 2.
 * 3.使用分布式锁，必须要先提交事务在释放锁，否则下一个锁拿到的数据就是上一次未提交的数据
 *
 * @author llxiao
 * @version 1.0, 2019/10/17 09:33
 * @since JDK 1.8
 */
public interface StockDemoService
{
    /**
     * 预占库存
     *
     * @param stockDemo
     * @return
     */
    long preStock(StockDemo stockDemo);

    /**
     * 释放库存
     *
     * @param stockDemo
     * @return
     */
    long releaseStock(StockDemo stockDemo);

    /**
     * 出库
     *
     * @param stockDemo
     * @return
     */
    long outHourse(StockDemo stockDemo);

    /**
     * [简要描述]:添加总库存<br/>
     * [详细描述]:<br/>
     *
     * @param stockDemo :
     * @return long
     * llxiao  2019/10/19 - 8:41
     **/
    long addStock(StockDemo stockDemo);

    /**
     * 获取订单信息
     * @param orderNo
     * @return
     */
    OrderDemo getOrderByNo(String orderNo);
}
