/*
 * Winner
 * 文件名  :SearchRequestDo.java
 * 创建人  :llxiao
 * 创建时间:2018年1月15日
 */

package com.xiao.spring.cloud.search.dto;

import lombok.Data;

/**
 * [简要描述]:请求查询接口参数<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月15日
 * @since Purcotton-Search B01
 */
@Data
public class SearchRequestDo
{
    /**
     * 关键字
     */
    private String keyWords;

    /**
     * 页数，默认1
     */
    private Integer pageNo = 1;

    /**
     * 也显示条数 10条
     */
    private Integer pageSize = 10;

    /**
     * 排序字段0默认排序，1价格排序，2销量排序，3好评度排序，4上架时间排序
     */
    private Integer sortFeild = 0;

    /**
     * 新品 0否，1是 -1默认不处理
     */
    private Integer newly = -1;

    /**
     * 有货 0有，1无 -1默认不处理
     */
    private Integer hasStock = -1;

    /**
     * 运营分类
     */
    private String oprtCatNo;

    /**
     * 排序类型，默认将序 DESC
     */
    private int sort = 0;

    /**
     * 价格区间 最小值
     */
    private float minPrice;

    /**
     * 价格区间 最大值
     */
    private float maxPrice;

    /**
     * 索引名称
     */
    private String index;
}
