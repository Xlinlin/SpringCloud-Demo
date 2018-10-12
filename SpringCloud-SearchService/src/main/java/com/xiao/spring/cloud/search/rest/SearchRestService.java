package com.xiao.spring.cloud.search.rest;

import com.xiao.skywalking.demo.common.logaspect.LogAnnotation;
import com.xiao.spring.cloud.search.dto.PaginationDo;
import com.xiao.spring.cloud.search.dto.SearchRequestDo;
import com.xiao.spring.cloud.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @LogAnnotation
    public PaginationDo search(@RequestBody SearchRequestDo searchRequestDo)
    {
        //索引为必填
        if (StringUtils.isBlank(searchRequestDo.getIndex()))
        {
            log.error("搜索的index不能为空!");
            return new PaginationDo();
        }
        return searchService.search(searchRequestDo);
    }
}
