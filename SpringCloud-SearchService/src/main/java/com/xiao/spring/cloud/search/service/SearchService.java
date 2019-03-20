package com.xiao.spring.cloud.search.service;

import com.xiao.spring.cloud.search.dto.SearchCommodityResultDo;
import com.xiao.spring.cloud.search.dto.SearchMenusDo;
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
    /**
     * [简要描述]:搜索<br/>
     * [详细描述]:<br/>
     *
     * @param searchRequestDo :
     * @return PaginationDo
     * llxiao  2018/10/13 - 9:29
     **/
    SearchCommodityResultDo search(SearchRequestDo searchRequestDo);

    /**
     * [简要描述]: 查询聚和菜单
     * [详细描述]:<br/>
     * @param searchRequestDo : 查询条件
     * @return com.purcotton.omni.search.dto.SearchMenusDo
     * mjye  2019-02-28 - 16:41
     **/
    SearchMenusDo searchMenu(SearchRequestDo searchRequestDo);

    /**
     * [简要描述]: 根据查询条件获取商品总数
     * [详细描述]:<br/>
     * @param searchRequestDo : 查询条件
     * @return int
     * mjye  2019-03-05 - 14:38
     **/
    long commodityTotal(SearchRequestDo searchRequestDo);
}
