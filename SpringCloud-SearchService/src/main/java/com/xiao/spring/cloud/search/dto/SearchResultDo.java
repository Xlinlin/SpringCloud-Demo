/*
 * Winner
 * 文件名  :SearchResponse.java
 * 创建人  :llxiao
 * 创建时间:2018年1月11日
 */

package com.xiao.spring.cloud.search.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * [简要描述]:搜索结果<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月11日
 * @since Purcotton-Search B01
 */
public class SearchResultDo
{
    /**
     * 返回文档
     */
    private ElasticSearchDoc doc;
    /**
     * 标题高亮字段
     */
    private String highlightTitle;
    /**
     * 子标题高亮字段
     */
    private String highlightSubTitle;

    /**
     * [简要描述]:替换返回的索引文档中高亮字段<br/>
     * [详细描述]:<br/>
     *
     * @return
     */
    public SearchResultDo setHighlight()
    {
        if (StringUtils.isNotBlank(highlightTitle))
        {
            doc.setTitle(highlightTitle);
        }
        if (StringUtils.isNoneBlank(highlightSubTitle))
        {
            doc.setSubTitle(highlightSubTitle);
        }
        return this;
    }

    /**
     * 返回doc属性
     *
     * @return doc属性
     */
    public ElasticSearchDoc getDoc()
    {
        return doc;
    }

    /**
     * 设置doc属性
     *
     * @param doc doc属性
     */
    public void setDoc(ElasticSearchDoc doc)
    {
        this.doc = doc;
    }

    /**
     * 返回highlightTitle属性
     *
     * @return highlightTitle属性
     */
    public String getHighlightTitle()
    {
        return highlightTitle;
    }

    /**
     * 设置highlightTitle属性
     *
     * @param highlightTitle highlightTitle属性
     */
    public void setHighlightTitle(String highlightTitle)
    {
        this.highlightTitle = highlightTitle;
    }

    /**
     * 返回highlightSubTitle属性
     *
     * @return highlightSubTitle属性
     */
    public String getHighlightSubTitle()
    {
        return highlightSubTitle;
    }

    /**
     * 设置highlightSubTitle属性
     *
     * @param highlightSubTitle highlightSubTitle属性
     */
    public void setHighlightSubTitle(String highlightSubTitle)
    {
        this.highlightSubTitle = highlightSubTitle;
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @return
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
