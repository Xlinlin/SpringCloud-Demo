/*
 * Winner 
 * 文件名  :AnalyzeType.java
 * 创建人  :llxiao
 * 创建时间:2018年1月13日
*/

package com.xiao.spring.cloud.search.es.common;

/**
 * [简要描述]:分析器类型<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年1月13日
 * @since Purcotton-Search B01
 */
public enum AnalyzeType
{
    /**
     * IK中文标准分词器,eg:我是中国人 -->"我","是","中国人"
     */
    IK_SMART("ik_smart"),
    /**
     * IM中文最大化分词,eg:我是中国人 --> "我","是","中国","中国人"
     */
    IK_MAX_WORD("ik_max_word"),

    /**
     * ES默认中文分词器，会将中文分词成一个一个。eg:中国 --> "中","国"
     */
    STANDARD("standard");

    private String type;

    private AnalyzeType(String type)
    {
        this.type = type;
    }

    /**
     * 返回type属性
     * 
     * @return type属性
     */
    public String getType()
    {
        return type;
    }

}
