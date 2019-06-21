package com.xiao.springcloud.demo.common.cache.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * [简要描述]: 使用fastjson序列化的类
 * [详细描述]:
 *
 * @author zhdong
 * @version 1.0,  2018/9/21
 * @since JDK 1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SerializerObject implements Serializable
{

    private Object value;

}

