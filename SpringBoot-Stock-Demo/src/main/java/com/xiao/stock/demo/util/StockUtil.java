package com.xiao.stock.demo.util;/**
 * [简要描述]:
 * [详细描述]:
 *
 * @since JDK 1.8
 */

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/10/17 11:38
 * @since JDK 1.8
 */
public class StockUtil
{
    private static final String STOCK_LOCK_PATH = "/stock/";
    private static final String ORDER_LOCK_PATH = "/order/";

    /**
     * 店铺+产品编号    获取redisson 锁
     *
     * @return
     */
    public static String getLockPath(String productNo, String shopCode)
    {
        return STOCK_LOCK_PATH + shopCode + '/' + productNo;
    }

    /**
     * 订单编号 redisson 锁
     *
     * @param orderNo
     * @return
     */
    public static String getOrderLockPath(String orderNo)
    {
        return ORDER_LOCK_PATH + orderNo;
    }
}
