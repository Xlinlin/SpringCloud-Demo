package com.xiao.spring.cloud.search.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * [简要描述]:C端筛选聚合菜单
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2019-02-23 10:46
 * @since JDK 1.8
 */
@Data
public class SearchMenusDo
{
    /**
     * 品牌
     **/
    private Set<SearchBrandDo> brandName;

    /**
     * 运营分类
     */
    private Set<SearchCategoryDo> oprtCatName;

    /**
     * 价格区间
     */
    private List<ShopPriceRangeDto> price;

    /**
     * SKU规格属性
     */
    private Set<SearchCommodityPropertyDo> skuProps;

    /**
     * 商品扩展属性
     */
    private Set<SearchCommodityPropertyDo> extProps;

    /**
     * 查询所用时间(单位:毫秒)
     */
    private long took;

}