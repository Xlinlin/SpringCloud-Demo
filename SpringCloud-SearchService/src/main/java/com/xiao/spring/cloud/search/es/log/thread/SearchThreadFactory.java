/*
 * Winner 
 * 文件名  :SearchThreadFactory.java
 * 创建人  :llxiao
 * 创建时间:2018年2月23日
*/

package com.xiao.spring.cloud.search.es.log.thread;

import java.util.concurrent.ThreadFactory;

/**
 * [简要描述]:线程工厂<br/>
 * [详细描述]:<br/>
 *
 * @author llxiao
 * @version 1.0, 2018年2月23日
 * @since Purcotton-Search B01
 */
public class SearchThreadFactory implements ThreadFactory
{
    private static final String SEARCH_LOG_THRED = "Search-log-thread-";

    @Override
    public Thread newThread(Runnable r)
    {
        SearchLogThread t = new SearchLogThread(r, SEARCH_LOG_THRED);
        return t;
    }

}
