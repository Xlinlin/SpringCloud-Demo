/*
 * Winner 
 * 文件名  :OrderField.java
 * 创建人  :llxiao
 * 创建时间:2018年1月15日
*/

package com.xiao.spring.cloud.search.es.common;

/**
 * [简要描述]:<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月15日
 * @since Purcotton-Search B01
 */
public enum OrderField
{
    /**
     * 默认排序
     */
    DEFAULT(0),
    /**
     * 价格排序
     */
    PRICE(1),

    /**
     * 销售排序
     */
    SALES(2),
    /**
     * 好评度排序
     */
    COMMENTS(3),

    /**
     * 上架时间排序
     */
    SLAESTIME(4);

    private int type;

    private OrderField(int type)
    {
        this.type = type;
    }

    /**
     * 返回type属性
     * 
     * @return type属性
     */
    public int getType()
    {
        return type;
    }

}
