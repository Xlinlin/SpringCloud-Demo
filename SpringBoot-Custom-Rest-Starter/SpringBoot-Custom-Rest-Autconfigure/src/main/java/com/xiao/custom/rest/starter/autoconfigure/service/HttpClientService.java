package com.xiao.custom.rest.starter.autoconfigure.service;

import com.xiao.custom.rest.starter.autoconfigure.dto.Request;
import org.springframework.http.ResponseEntity;

/**
 * [简要描述]: http服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/4/23 19:53
 * @since JDK 1.8
 */
public interface HttpClientService
{
    /**
     * [简要描述]:发起post请求<br/>
     * [详细描述]:<br/>
     *
     * @param request : 请求参数
     * @return T
     * llxiao  2019/4/23 - 19:56
     **/
    <T> T doForObject(Request request);

    <T> T doRequest(Request request);

    <T> T getForObject(Request request);

    /**
     * [简要描述]:formdata 获取请求Response<br/>
     * [详细描述]:<br/>
     *
     * @param request :
     * @return org.springframework.http.ResponseEntity<T>
     * llxiao  2019/8/26 - 16:33
     **/
    <T> ResponseEntity<T> postFormData(Request request);
}
