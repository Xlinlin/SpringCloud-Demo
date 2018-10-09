package com.xiao.springcloud.test;

import org.junit.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/9 11:07
 * @since JDK 1.8
 */
public class SearchManagerTest extends SearchApplicationTest
{
    @Test
    public void testGetByCommoNo() throws Exception
    {
        this.isPrint = true;
        String url = "/search/manager/getById";
        Map<String, String> params = new HashMap<>(1);
        params.put("id", "002000000662");
        params.put("index", "purcotton");
        // 字符串包含Contains
        //        this.testBasePostApi(url, params, null)
        //                .andExpect(MockMvcResultMatchers.content().string(new Contains("002000000662")));
        // json ID处理
        this.testBasePostApi(url, params, null).andExpect(MockMvcResultMatchers.jsonPath("$.id").value("002000000662"));
    }
}
