package com.xiao.springcloud.demo.common.sign.util;

import com.alibaba.fastjson.JSONObject;
import com.xiao.springcloud.demo.common.sign.SignConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author xiaolinlin
 * @version 1.0, 2020/2/21 18:35
 * @since JDK 1.8
 */
@Slf4j
public class HttpRequestUtils
{
    /**
     * [简要描述]:获取request中所有参数，get,post,json,form<br/>
     * [详细描述]:<br/>
     *
     * @param request :
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 18:59
     **/
    public static String getAllParams(HttpServletRequest request) throws IOException
    {
        //获取URL上的参数
        String urlParams = "";
        // get请求不需要拿body参数
        if (!HttpMethod.GET.name().equals(request.getMethod()))
        {
            String contentType = request.getContentType();
            if (SignConstants.JSON_TYPE.equals(contentType))
            {
                urlParams += getJsonParams(request);
            }
            String formParams = getFormParams(request);
            if (StringUtils.isNotBlank(formParams))
            {
                urlParams += getFormParams(request);
            }
        }
        else
        {
            urlParams = getUrlParams(request);
        }
        return urlParams;
    }

    /**
     * [简要描述]:获取表单参数<br/>
     * [详细描述]:<br/>
     *
     * @param request :
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 18:59
     **/
    private static String getFormParams(HttpServletRequest request)
    {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements())
        {
            String pName = parameterNames.nextElement();
            String pValue = request.getParameter(pName);
            sb.append(pName);
            sb.append(pValue);
        }
        return sb.toString();
    }

    /**
     * [简要描述]:获取JSON消息体参数<br/>
     * [详细描述]:<br/>
     *
     * @param request :
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 19:00
     **/
    public static String getJsonParams(final HttpServletRequest request) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str = "";
        StringBuilder jsonSb = new StringBuilder();
        //一行一行的读取body体里面的内容；
        while ((str = reader.readLine()) != null)
        {
            jsonSb.append(str);
        }
        return JSONObject.parseObject(jsonSb.toString()).toJSONString();
    }

    /**
     * [简要描述]:获取url参数<br/>
     * [详细描述]:<br/>
     *
     * @param request :
     * @return java.lang.String
     * xiaolinlin  2020/2/21 - 19:00
     **/
    public static String getUrlParams(HttpServletRequest request)
    {
        String characterEncoding = request.getCharacterEncoding();
        if (StringUtils.isBlank(characterEncoding))
        {
            characterEncoding = SignConstants.UTF_8;
        }
        String param = "";
        try
        {
            param = URLDecoder.decode(request.getQueryString(), characterEncoding);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error("不支持的编码格式!");
        }
        StringBuilder urlParams = new StringBuilder();
        String[] params = param.split("&");
        for (String s : params)
        {
            int index = s.indexOf("=");
            urlParams.append(s.substring(0, index));
            urlParams.append(s.substring(index + 1));
        }
        return urlParams.toString();
    }
}
