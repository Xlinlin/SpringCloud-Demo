package com.xiao.custom.rest.example.template;

import com.xiao.custom.rest.example.RestTemplateStarterAppTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * [简要描述]: resttemplate测试类
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/30 10:31
 * @since JDK 1.8
 */
public class RestTemplateTest extends RestTemplateStarterAppTest
{
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRest()
    {

    }
}
