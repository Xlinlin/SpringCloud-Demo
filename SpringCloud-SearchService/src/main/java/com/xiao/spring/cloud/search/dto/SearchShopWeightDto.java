package com.xiao.spring.cloud.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author mjye
 * @version 1.0, 2019-02-25 17:24
 * @since JDK 1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchShopWeightDto
{
    // id主键
    private long id;
    // 店铺编码
    private String shopCode;
    // 权重因子
    private Integer weightFactor;
    // 权重因子名称
    private String weightName;
    private String englishName;
}