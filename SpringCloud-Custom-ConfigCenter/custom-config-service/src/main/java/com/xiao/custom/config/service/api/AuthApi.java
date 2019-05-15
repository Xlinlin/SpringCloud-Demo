package com.xiao.custom.config.service.api;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;
import com.xiao.custom.config.service.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]: 鉴权服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/8 17:56
 * @since JDK 1.8
 */
@RestController
@RequestMapping(value = "/auth")
@Slf4j
public class AuthApi
{
    @Autowired
    private AuthService authService;

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @RequestMapping("/findByUsername")
    public AuthUser findByUsername(@RequestParam("username") String username)
    {
        return authService.findByUsername(username);
    }

    /**
     * 创建新用户
     *
     * @param userDetail
     */
    @RequestMapping("/insert")
    public void insert(@RequestBody AuthUser userDetail)
    {
        authService.insert(userDetail);
    }

    /**
     * 创建用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @RequestMapping("/insertRole")
    public int insertRole(@RequestParam("userId") long userId, @RequestParam("roleId") long roleId)
    {
        return authService.insertRole(userId, roleId);
    }

    /**
     * 根据角色id查找角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/findRoleById")
    public Role findRoleById(long roleId)
    {
        return authService.findRoleById(roleId);
    }

    /**
     * 根据用户id查找该用户角色
     *
     * @param userId
     * @return
     */
    @RequestMapping("/findRoleByUserId")
    public Role findRoleByUserId(long userId)
    {
        return authService.findRoleByUserId(userId);
    }
}
