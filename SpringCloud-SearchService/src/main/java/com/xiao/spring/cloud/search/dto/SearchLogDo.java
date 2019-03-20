/*
 * Winner 
 * 文件名  :SearchLogDo.java
 * 创建人  :llxiao
 * 创建时间:2018年2月23日
*/

package com.xiao.spring.cloud.search.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;

/**
 * [简要描述]:搜索日志数据<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年2月23日
 * @since Purcotton-Search B01
 */
public class SearchLogDo
{
    /**
     * 正常搜索
     */
    public static final int SUCCESS_SEARCH = 0;

    /**
     * 没有结果的搜索
     */
    public static final int NOT_RESULT_SEARCH = 1;

    /**
     * 错误的搜索
     */
    public static final int ERROR_RESULT_SEARCH = 2;

    /**
     * 未分词
     */
    public static final int UN_PARTICIPLE = 0;

    /**
     * IK_SMART分词
     */
    public static final int IK_SMART = 1;

    /**
     * IK_MAX_WORD分词
     */
    public static final int IK_MAX_WORD = 2;

    /**
     * ES默认分词，全拆
     */
    public static final int ES_DEFAULT = 3;

    /**
     * 主键
     */
    private Long id;

    /**
     * 搜索关键字
     */
    private String keyWords;

    /**
     * 分词关键字
     */
    private String participle;

    /**
     * 分词状态，0未分词1标准分词2普通分词
     */
    private int participleStats;

    /**
     * 搜索查询语句
     */
    private String searchQueryDSL;

    /**
     * 搜索返回结果
     */
    private String result;

    /**
     * 搜索状态0成功1失败
     */
    private int searchStats;

    /**
     * 搜索开始时间
     */
    private LocalDateTime startTime;

    /**
     * 搜索结束时间
     */
    private LocalDateTime endTime;

    /**
     * 搜索花费时间
     */
    private Long costTime;

    /**
     * 异常信息
     */
    private String exceptionMsg;

    /**
     * 请求参数
     */
    private String request;

    /**
     * 返回id属性
     * 
     * @return id属性
     */
    public Long getId()
    {
        return id;
    }

    /**
     * 设置id属性
     * 
     * @param id
     *            id属性
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * 返回keyWords属性
     * 
     * @return keyWords属性
     */
    public String getKeyWords()
    {
        return keyWords;
    }

    /**
     * 设置keyWords属性
     * 
     * @param keyWords
     *            keyWords属性
     */
    public void setKeyWords(String keyWords)
    {
        this.keyWords = keyWords;
    }

    /**
     * 返回result属性
     * 
     * @return result属性
     */
    public String getResult()
    {
        return result;
    }

    /**
     * 设置result属性
     * 
     * @param result
     *            result属性
     */
    public void setResult(String result)
    {
        this.result = result;
    }

    /**
     * 返回searchStats属性
     * 
     * @return searchStats属性
     */
    public int getSearchStats()
    {
        return searchStats;
    }

    /**
     * 设置searchStats属性
     * 
     * @param searchStats
     *            searchStats属性
     */
    public void setSearchStats(int searchStats)
    {
        this.searchStats = searchStats;
    }

    /**
     * 返回startTime属性
     * 
     * @return startTime属性
     */
    public LocalDateTime getStartTime()
    {
        return startTime;
    }

    /**
     * 设置startTime属性
     * 
     * @param startTime
     *            startTime属性
     */
    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }

    /**
     * 返回endTime属性
     * 
     * @return endTime属性
     */
    public LocalDateTime getEndTime()
    {
        return endTime;
    }

    /**
     * 设置endTime属性
     * 
     * @param endTime
     *            endTime属性
     */
    public void setEndTime(LocalDateTime endTime)
    {
        this.endTime = endTime;
    }

    /**
     * 返回costTime属性
     * 
     * @return costTime属性
     */
    public Long getCostTime()
    {
        return costTime;
    }

    /**
     * 设置costTime属性
     * 
     * @param costTime
     *            costTime属性
     */
    public void setCostTime(Long costTime)
    {
        this.costTime = costTime;
    }

    /**
     * 返回searchQueryDSL属性
     * 
     * @return searchQueryDSL属性
     */
    public String getSearchQueryDSL()
    {
        return searchQueryDSL;
    }

    /**
     * 设置searchQueryDSL属性
     * 
     * @param searchQueryDSL
     *            searchQueryDSL属性
     */
    public void setSearchQueryDSL(String searchQueryDSL)
    {
        this.searchQueryDSL = searchQueryDSL;
    }

    /**
     * 返回exceptionMsg属性
     * 
     * @return exceptionMsg属性
     */
    public String getExceptionMsg()
    {
        return exceptionMsg;
    }

    /**
     * 设置exceptionMsg属性
     * 
     * @param exceptionMsg
     *            exceptionMsg属性
     */
    public void setExceptionMsg(String exceptionMsg)
    {
        this.exceptionMsg = exceptionMsg;
    }

    /**
     * 返回request属性
     * 
     * @return request属性
     */
    public String getRequest()
    {
        return request;
    }

    /**
     * 设置request属性
     * 
     * @param request
     *            request属性
     */
    public void setRequest(String request)
    {
        this.request = request;
    }

    /**
     * 返回participle属性
     * 
     * @return participle属性
     */
    public String getParticiple()
    {
        return participle;
    }

    /**
     * 设置participle属性
     * 
     * @param participle
     *            participle属性
     */
    public void setParticiple(String participle)
    {
        this.participle = participle;
    }

    /**
     * 返回participleStats属性
     * 
     * @return participleStats属性
     */
    public int getParticipleStats()
    {
        return participleStats;
    }

    /**
     * 设置participleStats属性
     * 
     * @param participleStats
     *            participleStats属性
     */
    public void setParticipleStats(int participleStats)
    {
        this.participleStats = participleStats;
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @return
     * @see
     *      Object#toString()
     */
    @Override
    public String toString()
    {

        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
