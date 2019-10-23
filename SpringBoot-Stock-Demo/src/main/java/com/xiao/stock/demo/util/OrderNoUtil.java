package com.xiao.stock.demo.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 订单枷锁工具
 */
@Component
@Slf4j
public class OrderNoUtil
{
    private static long workerId = 0;
    private static Snowflake snowflake;

    static
    {
        try
        {

            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        }
        catch (Exception e)
        {
            log.warn("获取机器 ID 失败", e);
            workerId = NetUtil.getLocalhost().hashCode();
        }
        workerId = workerId % 32;
        log.info("当前机器 workerId: {}", workerId);
        // workerId 0~31
        snowflake = IdUtil.createSnowflake(workerId, workerId + 1);
    }

    public synchronized long snowflakeId()
    {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId, long dataCenterId)
    {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, dataCenterId);
        return snowflake.nextId();
    }

    /**
     * 订单编号规则：14位毫秒时间戳+3位顺序编号
     *
     * @return
     */
    public String buildOrderNo()
    {
        return "O" + snowflakeId();
    }

    public String buildOrderItemNo()
    {
        return "OI" + snowflakeId();
    }

    /**
     * 快递单号
     *
     * @return
     */
    public String buildDeliveryNo()
    {
        return "D" + snowflakeId();
    }

    /**
     * 预售单号
     *
     * @return
     */
    public String buildAdvanceSaleOrderNo()
    {
        return "Y" + snowflakeId();
    }

    /**
     * 跨境购生成单号
     *
     * @return
     */
    public String buildHaitaoOrderNo()
    {
        return "H" + snowflakeId();
    }

    /**
     * [简要描述]:内购会生成订单,由I变更为O，便于识别<br/>
     * [详细描述]:<br/>
     *
     * @return java.lang.String
     * llxiao  2019/9/18 - 14:19
     **/
    public String buildInOrderNo()
    {
        return "N" + snowflakeId();
    }

    /**
     * 拼团单号
     *
     * @return
     */
    public String buildPintuanOrderNo()
    {
        return "P" + snowflakeId();
    }

    /**
     * 拼明细单号
     *
     * @return
     */
    public String buildPintuanOrderItemNo()
    {
        return "PI" + snowflakeId();
    }

    /**
     * 团号
     *
     * @return
     */
    public String buildTuanNo()
    {
        return "T" + snowflakeId();
    }

}
