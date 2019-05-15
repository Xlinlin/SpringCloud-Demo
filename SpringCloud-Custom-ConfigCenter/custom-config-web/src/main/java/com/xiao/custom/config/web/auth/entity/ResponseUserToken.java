package com.xiao.custom.config.web.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author JoeTao
 * createAt: 2018/9/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseUserToken
{
    private String token;
    private UserDetail userDetail;
}
