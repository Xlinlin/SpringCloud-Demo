package com.xiao.skywalking.demo.common.cache.dto;

import lombok.Data;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/10/15 17:18
 * @since JDK 1.8
 */
@Data
public class EntryDto<T>
{
    private String key;
    private T value;
}
