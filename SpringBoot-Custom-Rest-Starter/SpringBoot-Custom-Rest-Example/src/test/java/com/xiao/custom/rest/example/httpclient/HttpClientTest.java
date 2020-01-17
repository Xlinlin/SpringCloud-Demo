package com.xiao.custom.rest.example.httpclient;

import com.xiao.custom.rest.example.RestTemplateStarterAppTest;
import com.xiao.custom.rest.starter.autoconfigure.dto.Request;
import com.xiao.custom.rest.starter.autoconfigure.service.HttpClientService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * [简要描述]: HTTP client测试
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/11/30 10:32
 * @since JDK 1.8
 */
public class HttpClientTest extends RestTemplateStarterAppTest
{
    @Autowired
    private HttpClientService httpClientService;

    @Test
    public void testHttpGet()
    {
        Request request = new Request();
        request.setResponseType(String.class);
        request.setUri("http://www.baidu.com");
        String forObject = httpClientService.getForObject(request);
        System.out.println(forObject);
    }
}
