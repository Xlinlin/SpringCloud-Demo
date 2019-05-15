package com.xiao.custom.config.web.auth.controller;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;
import com.xiao.custom.config.web.auth.AuthContants;
import com.xiao.custom.config.web.auth.entity.ResponseUserToken;
import com.xiao.custom.config.web.auth.entity.User;
import com.xiao.custom.config.web.auth.entity.UserDetail;
import com.xiao.custom.config.web.auth.service.AuthUserService;
import com.xiao.custom.config.web.auth.util.ResultCode;
import com.xiao.custom.config.web.auth.util.ResultJson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author JoeTao
 * createAt: 2018/9/17
 */

@RestController
//@Api(description = "登陆注册及刷新token")
@RequestMapping("/user/auth")
public class AuthController
{
    private static final long ADMIN_TYPE = 1;

    @Autowired
    private AuthUserService authService;

    @PostMapping(value = "/login")
    //@ApiOperation(value = "登陆", notes = "登陆成功返回token,测试管理员账号:admin,123456;用户账号：les123,admin")
    public ResultJson<ResponseUserToken> login(String loginName, String password, HttpServletResponse response)
    {
        final ResponseUserToken result = authService.login(loginName, password);
        response.setHeader(AuthContants.TOKEN_HEADER, AuthContants.TOKEN_BEARER_START + result.getToken());
        return ResultJson.ok(result);
    }

    @GetMapping(value = "/logout")
    //@ApiOperation(value = "登出", notes = "退出登陆")
    //    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public ResultJson logout(HttpServletRequest request, HttpServletResponse response)
    {
        String token = request.getHeader(AuthContants.TOKEN_HEADER);
        if (token == null)
        {
            return ResultJson.failure(ResultCode.UNAUTHORIZED);
        }
        authService.logout(token);

        //清除session数据
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
        {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        request.getSession().invalidate();
        return ResultJson.ok();
    }

    @GetMapping(value = "/getInfo")
    //@ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    //    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public ResultJson getUser(HttpServletRequest request)
    {
        String username = (String) request.getAttribute(AuthContants.REQUEST_USER_NAME);
        UserDetail userDetail = authService.getByUsername(username);
        return ResultJson.ok(userDetail);
    }

    @PostMapping(value = "/sign")
    //@ApiOperation(value = "用户注册")
    public ResultJson sign(@RequestBody User user)
    {
        if (StringUtils.isAnyBlank(user.getName(), user.getPassword()))
        {
            return ResultJson.failure(ResultCode.BAD_REQUEST);
        }

        AuthUser authUser = new AuthUser();
        authUser.setUsername(user.getName());
        authUser.setPassword(user.getPassword());
        Role role = new Role();
        role.setId(ADMIN_TYPE);
        authUser.setRole(role);
        return ResultJson.ok(authService.register(authUser));
    }

    @GetMapping(value = "/refresh")
    //    @ApiOperation(value = "刷新token")
    public ResultJson refreshAndGetAuthenticationToken(HttpServletRequest request)
    {
        String token = request.getHeader(AuthContants.TOKEN_HEADER);
        ResponseUserToken response = authService.refresh(token);
        if (response == null)
        {
            return ResultJson.failure(ResultCode.BAD_REQUEST, "token无效");
        }
        else
        {
            return ResultJson.ok(response);
        }
    }
}
