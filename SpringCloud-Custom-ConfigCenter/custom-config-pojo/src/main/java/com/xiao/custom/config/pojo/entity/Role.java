package com.xiao.custom.config.pojo.entity;

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
public class Role
{
    private Long id;
    private String name;
    private String nameZh;
}
