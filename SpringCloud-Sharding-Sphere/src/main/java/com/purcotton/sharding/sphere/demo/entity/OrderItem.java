package com.purcotton.sharding.sphere.demo.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/14 11:11
 * @since JDK 1.8
 */
@Data
public class OrderItem implements Serializable
{
    private long orderItemId;
    private long orderId;
    private int userId;
    private String status;

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
