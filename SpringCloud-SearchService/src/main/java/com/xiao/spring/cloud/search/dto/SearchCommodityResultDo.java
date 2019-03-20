package com.xiao.spring.cloud.search.dto;

import lombok.Data;

import java.util.List;

/**
 * [简要描述]: 查询商品返回组装结果
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2019-03-01 14:17
 * @since JDK 1.8
 */
@Data
public class SearchCommodityResultDo
{
    private List<ElasticSearchDoc> commodityList;

    /**
     * 总数
     */
    private int total;

    /**
     * 页显示数量
     */
    private int pageSize;

    /**
     * 当前页数
     */
    private int pageNo;

    /**
     * 总页数
     */
    private int pageTotal;
}