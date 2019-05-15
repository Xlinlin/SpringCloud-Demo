package com.xiao.custom.config.web.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : JoeTao
 * createAt: 2018/9/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User
{

    //    @ApiModelProperty(value = "用户名", required = true)
    private String name;
    //    @ApiModelProperty(value = "密码", required = true)
    private String password;
}
