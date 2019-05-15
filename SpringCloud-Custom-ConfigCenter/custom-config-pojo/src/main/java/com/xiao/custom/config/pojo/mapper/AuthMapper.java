package com.xiao.custom.config.pojo.mapper;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.pojo.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author JoeTao
 * createAt: 2018/9/17
 */
@Repository
public interface AuthMapper
{
    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    AuthUser findByUsername(@Param("username") String username);

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
    int insertRole(@Param("userId") long userId, @Param("roleId") long roleId);

    /**
     * 根据角色id查找角色
     *
     * @param roleId
     * @return
     */
    Role findRoleById(@Param("roleId") long roleId);

    /**
     * 根据用户id查找该用户角色
     *
     * @param userId
     * @return
     */
    Role findRoleByUserId(@Param("userId") long userId);
}
