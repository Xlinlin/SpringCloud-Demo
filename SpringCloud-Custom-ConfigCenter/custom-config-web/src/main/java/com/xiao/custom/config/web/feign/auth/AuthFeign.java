package com.xiao.custom.config.web.feign.auth;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;
import com.xiao.custom.config.web.commo.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * [简要描述]: 鉴权服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/8 17:56
 * @since JDK 1.8
 */
@FeignClient(value = Constants.CONFIG_SERVICE)
@RequestMapping(value = "/auth")
public interface AuthFeign
{
    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @RequestMapping("/findByUsername")
    AuthUser findByUsername(@RequestParam("username") String username);

    /**
     * 创建新用户
     *
     * @param userDetail
     */
    @RequestMapping("/insert")
    void insert(@RequestBody AuthUser userDetail);

    /**
     * 创建用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @RequestMapping("/insertRole")
    int insertRole(@RequestParam("userId") long userId, @RequestParam("roleId") long roleId);

    /**
     * 根据角色id查找角色
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/findRoleById")
    Role findRoleById(long roleId);

    /**
     * 根据用户id查找该用户角色
     *
     * @param userId
     * @return
     */
    @RequestMapping("/findRoleByUserId")
    Role findRoleByUserId(long userId);
}
