/*
 * Winner 
 * 文件名  :SearchLogService.java
 * 创建人  :llxiao
 * 创建时间:2018年2月23日
*/

package com.xiao.spring.cloud.search.es.log;

import com.xiao.spring.cloud.search.dto.SearchLogDo;

/**
 * [简要描述]:搜索日志服务<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年2月23日
 * @since Purcotton-Search B01
 */
public interface ISearchLogService
{
    /**
     * [简要描述]:添加一个搜索日志<br/>
     * [详细描述]:<br/>
     * 
     * @author llxiao
     * @param searchLog
     *            搜索日志
     */
    void addSearchLog(SearchLogDo searchLog);
}
