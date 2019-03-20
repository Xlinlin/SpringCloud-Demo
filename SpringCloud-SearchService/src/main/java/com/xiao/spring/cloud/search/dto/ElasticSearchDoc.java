/*
 * Winner
 * 文件名  :ElasticSearchDoc.java
 * 创建人  :llxiao
 * 创建时间:2018年1月11日
 */

package com.xiao.spring.cloud.search.dto;

import lombok.Data;

import java.util.Date;

/**
 * [简要描述]:Elastic search 存储文档<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月11日
 * @since Purcotton-Search B01
 */
@Data
public class ElasticSearchDoc
{
    /**
     * 文档id
     */
    private String id;

    /**
     * 关键字（商品表的卖点+货品表的商品标题+货品表的规格）
     */
    private String keyWords;

    /**
     * 商品全局唯一SPU
     */
    private String commodityNo;

    /**
     * 商品业务SPU编码
     */
    private String commodityCode;

    /**
     * 默认SKU编号(全局唯一)
     */
    private String defProdNo;

    /**
     * 默认SKU业务编码
     */
    private String defProdCode;

    /**
     * 品牌编号
     */
    private String brandNo;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 标题,商品标题，SKU名称
     */
    private String title;

    /**
     * 子标题（卖点）
     */
    private String subTitle;

    private String skuTitle;

    /**
     * 扩展属性(JSON列表存储(组-名称-值))
     */
    private String extProps;

    /**
     * JSON列表存储(SKU规格属性)
     */
    private String skuProps;

    /**
     * 产地
     */
    private String productArea;

    /**
     * 牌价
     */
    private Double orgPrice;

    /**
     * 售价(售价由调价刷新)
     */
    private Double salePrice;

    /**
     * 商品运营分类编号（多个用逗号隔开）
     */
    private String oprtCatNo;

    /**
     * 运营分类名称(以"-"隔开，多个以","隔开)
     */
    private String oprtCatName;

    /**
     * 标签集合
     * JSON列表存储(名称-颜色)
     */
    private String labels;

    /**
     * 上架时间
     */
    private Date saleTime;

    /**
     * 商品首图
     */
    private String picUrl;

    /**
     * 库存(库存定时刷新)
     */
    private Long stock;

    /**
     * 是否海淘(0否，1是)
     */
    private Integer haitao;

    /**
     * 销量
     */
    private Long salesVolume;

    /**
     * 索引名称/或店铺名称
     */
    private String index;

    //*****************************以下是预留字段****************************

    /**
     * 商品分类编号
     */
    private String commoCatNo;

    /**
     * 新品 1新品，0默认
     */
    private Integer newly = 0;

    /**
     * 评论数量
     */
    private Integer comments;

    /**
     * 活动属性 折扣信息
     */
    private String discountRate;

    //****************************用来聚合的字段***********************************
    //保存三级分类
    private String categoryName;

    //JSON列表存储
    private String defSkuProp;
}
