package com.xiao.custom.config.web.auth.service;

import com.xiao.custom.config.pojo.entity.AuthUser;
import com.xiao.custom.config.web.auth.entity.ResponseUserToken;
import com.xiao.custom.config.web.auth.entity.UserDetail;

/**
 * [简要描述]: 登陆验证
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/5/8 10:22
 * @since JDK 1.8
 */
public interface AuthUserService
{
    /**
     * 注册用户
     *
     * @param userDetail
     * @return
     */
    UserDetail register(AuthUser userDetail);

    /**
     * 登陆
     *
     * @param username
     * @param password
     * @return
     */
    ResponseUserToken login(String username, String password);

    /**
     * 登出
     *
     * @param token
     */
    void logout(String token);

    /**
     * 刷新Token
     *
     * @param oldToken
     * @return
     */
    ResponseUserToken refresh(String oldToken);

    /**
     * 根据Token获取用户信息
     *
     * @param token
     * @return
     */
    UserDetail getUserByToken(String token);

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param token :
     * @return com.winner.config.center.web.auth.entity.UserDetail
     * llxiao  2019/5/9 - 17:24
     **/
    UserDetail getByToken(String token);

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param username :
     * @return com.winner.config.center.web.auth.entity.UserDetail
     * llxiao  2019/5/9 - 17:24
     **/
    UserDetail getByUsername(String username);
}
