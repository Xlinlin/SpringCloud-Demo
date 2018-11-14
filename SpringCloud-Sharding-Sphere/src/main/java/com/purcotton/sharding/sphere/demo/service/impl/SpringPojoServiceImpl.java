package com.purcotton.sharding.sphere.demo.service.impl;

import com.purcotton.sharding.sphere.demo.entity.Order;
import com.purcotton.sharding.sphere.demo.entity.OrderItem;
import com.purcotton.sharding.sphere.demo.repository.OrderItemRepository;
import com.purcotton.sharding.sphere.demo.repository.OrderRepository;
import com.purcotton.sharding.sphere.demo.service.BasisCommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/14 11:25
 * @since JDK 1.8
 */
@Service
@Transactional
public class SpringPojoServiceImpl extends BasisCommonService
{
    @Resource
    private OrderRepository orderRepository;

    @Resource
    private OrderItemRepository orderItemRepository;

    @Override
    protected OrderRepository getOrderRepository()
    {
        return orderRepository;
    }

    @Override
    protected OrderItemRepository getOrderItemRepository()
    {
        return orderItemRepository;
    }

    @Override
    protected Order newOrder()
    {
        return new Order();
    }

    @Override
    protected OrderItem newOrderItem()
    {
        return new OrderItem();
    }
}
