package com.xiao.custom.config.pojo.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @author : JoeTao
 * createAt: 2018/9/14
 */
@Data
public class AuthUser
{
    private long id;
    private String username;
    private String password;
    private String nickname;
    /**
     * 最后一次重置密码时间
     */
    private Timestamp lastResetTime;

    private Role role;
}
