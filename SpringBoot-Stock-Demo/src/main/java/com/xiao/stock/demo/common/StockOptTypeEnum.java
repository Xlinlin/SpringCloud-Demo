package com.xiao.stock.demo.common;

/**
 * [简要描述]: 库存操作记录
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/10/17 15:13
 * @since JDK 1.8
 */
public enum StockOptTypeEnum
{
    LOCK_STOCK("LOCK_STOCK", "预占锁库存"),
    RELEASE_STOCK("RELEASE_STOCK", "释放预占库存"),
    OUT_WAREHOUSE_STOCK("OUT_WAREHOUSE_STOCK", "订单出库"),
    ADD_STOCK("ADD_STOCK","添加库存");

    private String optType;
    private String optName;

    StockOptTypeEnum(String optType, String optName)
    {
        this.optType = optType;
        this.optName = optName;
    }

    public String getOptType()
    {
        return optType;
    }

    public String getOptName()
    {
        return optName;
    }
}
