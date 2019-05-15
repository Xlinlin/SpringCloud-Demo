package com.xiao.custom.config.web.auth.config;

import com.xiao.custom.config.web.auth.AuthContants;
import com.xiao.custom.config.web.auth.entity.UserDetail;
import com.xiao.custom.config.web.auth.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token校验
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter
{

    @Resource
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException
    {
        String authToken = request.getHeader(AuthContants.TOKEN_HEADER);

        // 以 Bearer 开头的token
        if (StringUtils.isNotEmpty(authToken) && authToken.startsWith(AuthContants.TOKEN_BEARER_START))
        {
            authToken = authToken.substring(AuthContants.TOKEN_BEARER_START.length());
            accessToken(request, authToken);
        }
        chain.doFilter(request, response);
    }

    private void accessToken(HttpServletRequest request, String authToken)
    {
        String username = jwtUtils.getUsernameFromToken(authToken);

        if (jwtUtils.containToken(username, authToken) && username != null
                && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            // 可以考虑做分布式session
            UserDetail userDetail = jwtUtils.getUserFromToken(authToken);
            if (jwtUtils.validateToken(authToken, userDetail))
            {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail
                        .getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("Authenticated userDetail {}, setting security context", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        //存储用户登录名，下一步流程处理
        request.setAttribute(AuthContants.REQUEST_USER_NAME, username);
    }
}
