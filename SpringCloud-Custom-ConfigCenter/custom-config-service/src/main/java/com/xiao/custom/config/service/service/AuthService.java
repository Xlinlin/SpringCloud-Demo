package com.xiao.custom.config.service.service;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;

/**
 * [简要描述]: 鉴权服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/8 17:53
 * @since JDK 1.8
 */
public interface AuthService
{
    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    AuthUser findByUsername(String username);

    /**
     * 创建新用户
     *
     * @param userDetail
     */
    void insert(AuthUser userDetail);

    /**
     * 创建用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    int insertRole(long userId, long roleId);

    /**
     * 根据角色id查找角色
     *
     * @param roleId
     * @return
     */
    Role findRoleById(long roleId);

    /**
     * 根据用户id查找该用户角色
     *
     * @param userId
     * @return
     */
    Role findRoleByUserId(long userId);
}
