package com.xiao.custom.config.web.auth.service.impl;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;
import com.xiao.custom.config.web.auth.AuthContants;
import com.xiao.custom.config.web.auth.entity.ResponseUserToken;
import com.xiao.custom.config.web.auth.entity.UserDetail;
import com.xiao.custom.config.web.auth.exception.CustomException;
import com.xiao.custom.config.web.auth.service.AuthUserService;
import com.xiao.custom.config.web.auth.util.JwtUtils;
import com.xiao.custom.config.web.auth.util.ResultCode;
import com.xiao.custom.config.web.auth.util.ResultJson;
import com.xiao.custom.config.web.feign.auth.AuthFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * [简要描述]: 用户权限实现
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/8 10:27
 * @since JDK 1.8
 */
@Service
public class AuthUserServiceImpl implements AuthUserService
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("configUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private AuthFeign authApi;

    @Override
    public UserDetail register(AuthUser authUser)
    {
        final String username = authUser.getUsername();
        if (authApi.findByUsername(username) != null)
        {
            throw new CustomException(ResultJson.failure(ResultCode.BAD_REQUEST, "用户已存在"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = authUser.getPassword();
        authUser.setPassword(encoder.encode(rawPassword));
        authUser.setLastResetTime(new Timestamp(System.currentTimeMillis()));
        authApi.insert(authUser);
        long roleId = authUser.getRole().getId();
        Role role = authApi.findRoleById(roleId);
        authUser.setRole(role);
        authApi.insertRole(authUser.getId(), roleId);
        return new UserDetail(authUser);
    }

    @Override
    public ResponseUserToken login(String username, String password)
    {
        //用户验证
        final Authentication authentication = authenticate(username, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        //存储token
        jwtTokenUtil.putToken(username, token);
        return new ResponseUserToken(token, userDetail);

    }

    @Override
    public void logout(String token)
    {
        token = token.substring(AuthContants.TOKEN_BEARER_START.length());
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        jwtTokenUtil.deleteToken(userName);
    }

    @Override
    public ResponseUserToken refresh(String oldToken)
    {
        String token = oldToken.substring(AuthContants.TOKEN_BEARER_START.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetail userDetail = (UserDetail) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, userDetail.getLastPasswordResetDate()))
        {
            token = jwtTokenUtil.refreshToken(token);
            return new ResponseUserToken(token, userDetail);
        }
        return null;
    }

    @Override
    public UserDetail getUserByToken(String token)
    {
        token = token.substring(AuthContants.TOKEN_BEARER_START.length());
        return jwtTokenUtil.getUserFromToken(token);
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param token :
     * @return com.winner.config.center.web.auth.entity.UserDetail
     * llxiao  2019/5/9 - 17:24
     **/
    @Override
    public UserDetail getByToken(String token)
    {
        token = token.substring(AuthContants.TOKEN_BEARER_START.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return new UserDetail(authApi.findByUsername(username));
    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param username :
     * @return com.winner.config.center.web.auth.entity.UserDetail
     * llxiao  2019/5/9 - 17:24
     **/
    @Override
    public UserDetail getByUsername(String username)
    {
        return new UserDetail(authApi.findByUsername(username));
    }

    private Authentication authenticate(String username, String password)
    {
        try
        {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (DisabledException | BadCredentialsException e)
        {
            throw new CustomException(ResultJson.failure(ResultCode.LOGIN_ERROR, e.getMessage()));
        }
    }
}
