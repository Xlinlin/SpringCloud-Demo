package com.xiao.springcloud.test;

import org.junit.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/9 11:07
 * @since JDK 1.8
 */
public class SearchTest extends SearchApplicationTest
{

    @Test
    public void testSearch() throws Exception
    {
        this.isPrint = true;
        String json = "{\"index\":\"purcotton\",\"keyWords\":\"棉柔巾\"}";
        ResultActions actions = this.testBasePostApi("/search/keywords", null, json)
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageNo")
                        .value(1)); //使用Json path验证JSON 请参考http://goessner.net/articles/JsonPath/
    }
}
