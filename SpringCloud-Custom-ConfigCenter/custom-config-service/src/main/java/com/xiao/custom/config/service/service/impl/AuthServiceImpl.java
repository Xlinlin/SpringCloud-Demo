package com.xiao.custom.config.service.service.impl;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;
import com.xiao.custom.config.pojo.mapper.AuthMapper;
import com.xiao.custom.config.service.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * [简要描述]: 鉴权服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/8 17:54
 * @since JDK 1.8
 */
@Service
public class AuthServiceImpl implements AuthService
{
    @Autowired
    private AuthMapper authMapper;

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public AuthUser findByUsername(String username)
    {
        AuthUser authUser = null;
        if (StringUtils.isNotBlank(username))
        {
            authUser = authMapper.findByUsername(username);
            Role role = authMapper.findRoleByUserId(authUser.getId());
            authUser.setRole(role);
        }

        return authUser;
    }

    /**
     * 创建新用户
     *
     * @param userDetail
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(AuthUser userDetail)
    {
        if (null != userDetail)
        {
            authMapper.insert(userDetail);
        }

    }

    /**
     * 创建用户角色
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRole(long userId, long roleId)
    {
        return authMapper.insertRole(userId, roleId);
    }

    /**
     * 根据角色id查找角色
     *
     * @param roleId
     * @return
     */
    @Override
    public Role findRoleById(long roleId)
    {
        return authMapper.findRoleById(roleId);
    }

    /**
     * 根据用户id查找该用户角色
     *
     * @param userId
     * @return
     */
    @Override
    public Role findRoleByUserId(long userId)
    {
        return authMapper.findRoleByUserId(userId);
    }
}
