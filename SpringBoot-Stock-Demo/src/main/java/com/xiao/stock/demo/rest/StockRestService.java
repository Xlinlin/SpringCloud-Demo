package com.xiao.stock.demo.rest;

import com.xiao.stock.demo.common.StockOptTypeEnum;
import com.xiao.stock.demo.entity.OrderDemo;
import com.xiao.stock.demo.entity.StockDemo;
import com.xiao.stock.demo.service.StockDemoService;
import com.xiao.stock.demo.util.OrderNoUtil;
import com.xiao.stock.demo.util.StockUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * [简要描述]: 库存接口服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/10/17 17:28
 * @since JDK 1.8
 */
@RestController
@RequestMapping("/stock")
public class StockRestService
{
    @Autowired
    private StockDemoService stockDemoService;

    @Autowired
    private OrderNoUtil orderNoUtil;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 预占库存
     *
     * @param productNo
     * @param shopCode
     * @param num 下单数量
     * @return
     */
    @RequestMapping("/preStock")
    public int preStock(@RequestParam("productNo") String productNo, @RequestParam("shopCode") String shopCode,
            @RequestParam(value = "num", required = false) int num)
    {
        return optStock(productNo, shopCode, num, StockOptTypeEnum.LOCK_STOCK, orderNoUtil.buildOrderNo());
    }

    /**
     * 取消释放库存，实际释放的应该是订单明细商品的数量
     *
     * @param orderNo: 订单编号
     * @return
     */
    @RequestMapping("/releaseStock")
    public int releaseStock(@RequestParam("orderNo") String orderNo)
    {
        RLock fairLock = redissonClient.getFairLock(StockUtil.getOrderLockPath(orderNo));
        try
        {
            // 并发防止重复处理订单
            if (fairLock.tryLock())
            {
                OrderDemo orderDemo = stockDemoService.getOrderByNo(orderNo);
                return optStock(orderDemo.getProductNo(), orderDemo.getShopCode(), orderDemo
                        .getProductNum(), StockOptTypeEnum.RELEASE_STOCK, orderNo);
            }
        }
        finally
        {
            fairLock.unlock();
        }
        return 0;
    }

    /**
     * 出库，实际应该是订单明细里面的数据<br>
     * 仓库发货回写应该是具体的订单明细的商品数据和数量<br>
     *
     * @param orderNo: 订单编号
     * @return
     */
    @RequestMapping("/warehouse")
    public int warehouse(@RequestParam("orderNo") String orderNo)
    {
        RLock fairLock = redissonClient.getFairLock(StockUtil.getOrderLockPath(orderNo));
        try
        {
            if (fairLock.tryLock())
            {
                OrderDemo orderDemo = stockDemoService.getOrderByNo(orderNo);
                return optStock(orderDemo.getProductNo(), orderDemo.getShopCode(), orderDemo
                        .getProductNum(), StockOptTypeEnum.OUT_WAREHOUSE_STOCK, orderNo);
            }
        }
        finally
        {
            fairLock.unlock();
        }
        return 0;
    }

    @RequestMapping("/addStock")
    public int addStock(@RequestParam("productNo") String productNo, @RequestParam("shopCode") String shopCode,
            @RequestParam(value = "num", required = false) int num)
    {
        return optStock(productNo, shopCode, num, StockOptTypeEnum.ADD_STOCK, orderNoUtil.buildOrderNo());
    }

    /**
     * 库存操作
     *
     * @param productNo
     * @param shopCode
     * @param num 下单数量
     * @return
     */
    public int optStock(String productNo, String shopCode, int num, StockOptTypeEnum stockOptTypeEnum, String orderNo)
    {
        StockDemo stockDemo = new StockDemo();
        stockDemo.setShopCode(shopCode);
        stockDemo.setProductNo(productNo);
        stockDemo.setProductNum(num);
        stockDemo.setOrderNo(orderNo);

        Long result = 0L;
        switch (stockOptTypeEnum)
        {
            case LOCK_STOCK:
                result = stockDemoService.preStock(stockDemo);
                break;
            case RELEASE_STOCK:
                result = stockDemoService.releaseStock(stockDemo);
                break;
            case OUT_WAREHOUSE_STOCK:
                result = stockDemoService.outHourse(stockDemo);
                break;
            case ADD_STOCK:
                result = stockDemoService.addStock(stockDemo);
                break;
            default:
                break;
        }
        return result.intValue();
    }
}
