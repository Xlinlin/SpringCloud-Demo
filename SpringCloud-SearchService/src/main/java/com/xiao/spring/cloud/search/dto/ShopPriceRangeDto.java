package com.xiao.spring.cloud.search.dto;

import lombok.Data;

/**
 * [简要描述]:ES店铺商品价格区间
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2019-02-23 18:45
 * @since JDK 1.8
 */
@Data
public class ShopPriceRangeDto implements Comparable<ShopPriceRangeDto>
{
    private Long id;
    //店铺编码
    private String shopCode;
    //最低价格
    private String floorPrice;
    //最高价格
    private String highestPrice;
    //价格区间
    private String priceRange;
    //状态  1：启用  2：禁用  默认禁用
    private Integer status;
    //店铺名称
    private String shopName;

    @Override
    public int compareTo(ShopPriceRangeDto o)
    {
        return Integer.parseInt(this.getFloorPrice()) - Integer.parseInt(this.getFloorPrice());
    }
}