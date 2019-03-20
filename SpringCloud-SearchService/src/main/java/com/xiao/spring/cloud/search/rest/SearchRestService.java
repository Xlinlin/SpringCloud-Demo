package com.xiao.spring.cloud.search.rest;

import com.xiao.spring.cloud.search.dto.SearchCommodityResultDo;
import com.xiao.spring.cloud.search.dto.SearchMenusDo;
import com.xiao.spring.cloud.search.dto.SearchRequestDo;
import com.xiao.spring.cloud.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]: 基于springcloud提供搜索rest服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/8 16:15
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchRestService
{
    @Autowired
    private SearchService searchService;

    @RequestMapping("/keywords")
    public SearchCommodityResultDo search(@RequestBody SearchRequestDo searchRequestDo)
    {
        //索引为必填
        if (StringUtils.isBlank(searchRequestDo.getIndex()) || (StringUtils.isBlank(searchRequestDo.getKeyWords())
                && StringUtils.isBlank(searchRequestDo.getOprtCatNo())))
        {
            log.error("搜索的index或者keyWords不能为空!");
            return new SearchCommodityResultDo();
        }
        return searchService.search(searchRequestDo);
    }

    @RequestMapping("/searchMenu")
    public SearchMenusDo searchMenu(@RequestBody SearchRequestDo searchRequestDo)
    {
        //索引为必填
        if (StringUtils.isBlank(searchRequestDo.getIndex()) || (StringUtils.isBlank(searchRequestDo.getKeyWords())
                && StringUtils.isBlank(searchRequestDo.getOprtCatNo())))
        {
            log.error("搜索的index或者keyWords不能为空!");
            return new SearchMenusDo();
        }
        return searchService.searchMenu(searchRequestDo);
    }

    /**
     * [简要描述]: 根据查询条件获取商品总数
     * [详细描述]:
     * @param searchRequestDo : 查询条件
     * @return int
     * mjye  2019-03-05 - 14:35
     **/
    @RequestMapping("/commodityTotal")
    public Long commodityTotal(@RequestBody SearchRequestDo searchRequestDo)
    {
        //索引为必填
        if (StringUtils.isBlank(searchRequestDo.getIndex()) || (StringUtils.isBlank(searchRequestDo.getKeyWords())
                && StringUtils.isBlank(searchRequestDo.getOprtCatNo())))
        {
            log.error("搜索的index或者keyWords不能为空!");
            return 0L;
        }
        return searchService.commodityTotal(searchRequestDo);
    }
}
