package com.xiao.springcloud.rest.httpclient;

import com.xiao.springcloud.rest.RestTemplateStarterAppTest;
import com.xiao.springcloud.rest.stater.autoconfig.common.dto.Request;
import com.xiao.springcloud.rest.stater.autoconfig.common.service.HttpClientService;
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
