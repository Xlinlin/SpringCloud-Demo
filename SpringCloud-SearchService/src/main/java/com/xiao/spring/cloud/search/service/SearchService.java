package com.xiao.spring.cloud.search.service;

import com.xiao.spring.cloud.search.dto.PaginationDo;
import com.xiao.spring.cloud.search.dto.SearchRequestDo;

/**
 * [简要描述]: 搜索服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/8 09:58
 * @since JDK 1.8
 */
public interface SearchService
{
    PaginationDo search(SearchRequestDo searchRequestDo);
}
