package com.xiao.custom.config.web.auth.config;

import com.xiao.custom.config.web.auth.util.ResultCode;
import com.xiao.custom.config.web.auth.util.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限不足处理
 * createAt: 2018/9/21
 */
@Component("restAuthenticationAccessDeniedHandler")
@Slf4j
public class RestAccessDeniedHandler implements AccessDeniedHandler
{
    /**
     * 登陆状态下，权限不足执行该方法
     *
     * @param httpServletRequest
     * @param response
     * @param e
     * @exception IOException
     * @exception ServletException
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e)
            throws IOException
    {
        log.error("权限不足：" + e.getMessage());
        //浏览器方式
        webBrowser(response);
        // API接口方式
        //        api(response, e);
    }

    private void api(HttpServletResponse response, AccessDeniedException e) throws IOException
    {
        // 接口返回以json格式
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = ResultJson.failure(ResultCode.FORBIDDEN, e.getMessage()).toString();
        printWriter.write(body);
        printWriter.flush();
    }

    private void webBrowser(HttpServletResponse response) throws IOException
    {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.sendError(HttpStatus.FORBIDDEN.value(), "没有访问权限");
    }
}
