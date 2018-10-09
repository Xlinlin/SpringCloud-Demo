package com.xiao.springcloud.test;

import com.xiao.spring.cloud.search.SearchApplication;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.Iterator;
import java.util.Map;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/9 11:05
 * @since JDK 1.8
 */
@RunWith(SpringRunner.class)
// 单个controller测试
//@SpringBootTest(classes = MockServletContext.class)
// 全局 api测试
@SpringBootTest(classes = SearchApplication.class)
@WebAppConfiguration
public class SearchApplicationTest
{
    protected MockMvc mockMvc;

    // 是否打印请求信息
    protected boolean isPrint;

    // 全局api测试
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
        // 全局api测试
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        isPrint = false;
    }

    /**
     * [简要描述]:post请求<br/>
     * [详细描述]:KV和JSON参数二者都存在是优先取KV参数<br/>
     *
     * @param url : 请求地址
     * @param params :  请求参数 KV
     * @param jsonParams :  请求JSON参数
     * @return org.springframework.test.web.servlet.ResultActions
     * llxiao  2018/10/9 - 16:44
     **/
    protected ResultActions testBasePostApi(String url, Map<String, String> params, String jsonParams) throws Exception
    {
        //构建请求
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(url);

        if (MapUtils.isNotEmpty(params))
        {
            // 参数请求
            MultiValueMap<String, String> paramMap = new LinkedMultiValueMap();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            Map.Entry<String, String> entry;
            while (iterator.hasNext())
            {
                entry = iterator.next();
                paramMap.add(entry.getKey(), entry.getValue());
            }
            request.params(paramMap);
        }
        else if (StringUtils.isNotBlank(jsonParams))
        {
            request.contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonParams)
                    .accept(MediaType.APPLICATION_JSON_UTF8);
        }

        // 发起http请求
        ResultActions actions = mockMvc.perform(request);
        if (isPrint)
        {
            // 打印出request和response的详细信息，便于调试。
            actions.andDo(MockMvcResultHandlers.print());
        }
        // 期望返回 200
        actions.andExpect(MockMvcResultMatchers.status().isOk());
        if (MapUtils.isEmpty(params) && StringUtils.isNotBlank(jsonParams))
        {
            // JSON返回处理
            actions.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8));
        }
        return actions;
    }
}
