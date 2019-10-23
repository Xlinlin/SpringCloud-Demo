package com.xiao.stock.demo.service.impl;/**
 * [简要描述]:
 * [详细描述]:
 *
 * @since JDK 1.8
 */

import com.xiao.stock.demo.common.StockOptTypeEnum;
import com.xiao.stock.demo.entity.OrderDemo;
import com.xiao.stock.demo.entity.StockChangeLodDemo;
import com.xiao.stock.demo.entity.StockDemo;
import com.xiao.stock.demo.mapper.OrderDemoMapper;
import com.xiao.stock.demo.mapper.StockChangeLodDemoMapper;
import com.xiao.stock.demo.mapper.StockDemoMapper;
import com.xiao.stock.demo.service.StockDemoService;
import com.xiao.stock.demo.util.StockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/10/17 10:45
 * @since JDK 1.8
 */
@Service
@Slf4j
public class StockDemoServiceImpl implements StockDemoService
{
    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StockDemoMapper stockDemoMapper;

    @Autowired
    private StockChangeLodDemoMapper stockChangeLodDemoMapper;

    @Autowired
    private OrderDemoMapper orderDemoMapper;

    /**
     * 下单预占库存
     *
     * @param stockDemo
     * @return
     */
    @Override
    public long preStock(StockDemo stockDemo)
    {
        if (checkStock(stockDemo, StockOptTypeEnum.LOCK_STOCK.getOptType()))
        {
            return 0;
        }

        String productNo = stockDemo.getProductNo();
        String shopCode = stockDemo.getShopCode();

        SecureRandom secureRandom = null;
        try
        {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        // 公平锁
        final RLock fairLock = redissonClient.getFairLock(StockUtil.getLockPath(productNo, shopCode));
        // 异步 尝试加锁，最多等待50秒，上锁以后10秒自动解锁
        Future<Boolean> res = fairLock.tryLockAsync(50, 10, TimeUnit.SECONDS);

        try
        {
            //final boolean lock = fairLock.tryLock(50, 10, TimeUnit.SECONDS);
            if (res.get())
            {
                //System.out.println(Thread.currentThread().getName() + "Redis获得锁。。。" + System.currentTimeMillis());
                // 校验
                StockDemo stock = stockDemoMapper
                        .queryByProduct(stockDemo.getProductNo(), stockDemo.getShopCode(), secureRandom.nextInt(9999));
                if (null == stock)
                {
                    // 如果不存在直接抛异常扣库存失败
                    log.error("商品不存在库存信息：{}", stockDemo);
                    return 0;
                }
                // 需要再次从新获取，库存数据
                StockDemo tempStock = null;
                boolean preStatus = false;
                // 重试次数，5次更新机会
                int tryPreTimes = 0;
                while (null != stock)
                {
                    //System.out.println(Thread.currentThread().getName() + "查询的===========：" + stock);
                    int avaStock = stock.getAvaStock();
                    int productNum = stockDemo.getProductNum();
                    if (productNum > avaStock)
                    {
                        // 预占库存不足，抛异常，下单失败
                        log.error("下单失败，可用库存不足，单号：{}，店铺：{}", stockDemo.getOrderNo(), shopCode);
                        log.error("商品：{}，预占库存：{}，实际可用库存：{}", productNo, productNum, avaStock);
                        throw new Exception("下单失败，库存不足!");
                    }
                    int preStock = stock.getPreStock();

                    // 预占+ 可用-
                    int newAvaStock = avaStock - productNum;
                    int newPreStock = preStock + productNum;
                    tempStock = new StockDemo();
                    tempStock.setId(stock.getId());
                    // 旧库存 预占和可用库存
                    tempStock.setAvaStock(stock.getAvaStock());
                    tempStock.setPreStock(stock.getPreStock());
                    // 新库存 预占和可用库存
                    tempStock.setNewAvaStock(newAvaStock);
                    tempStock.setNewPreStock(newPreStock);

                    tempStock.setTotalStock(stock.getTotalStock());
                    //System.out.println(Thread.currentThread().getName() + "更新的===========：" + tempStock);

                    //尝试CAS，先比较旧值，再更新
                    if (optStock(tempStock) > 0)
                    {
                        // 库存扣减成功
                        log.info("下单成功，订单号：{}，预占库存数量：{}", stockDemo.getOrderNo(), productNum);
                        addOrder(stockDemo);
                        preStatus = true;
                        break;
                    }
                    else
                    {
                        // 库存扣减与预期的值不对，更新失败，尝试重新更新
                        log.info("库存扣减与预期的值不对，更新失败，尝试重新更新！");
                        //                                            tryPreTimes++;
                        stock = stockDemoMapper.queryByProduct(productNo, shopCode, secureRandom.nextInt(9999));
                    }
                }

                // 总库存
                tempStock.setTotalStock(stock.getTotalStock());
                tempStock.setNewTotalStock(stock.getTotalStock());

                // 预占成功处理日志
                return saveChangeLog(stockDemo, productNo, shopCode, stock, tempStock, preStatus, StockOptTypeEnum.LOCK_STOCK);
            }
            else
            {
                // 此处需要考虑下单失败后，补偿下单扣库存的
                log.error("下单失败，获取不到锁!");
                throw new Exception("下单失败，获取不到锁");
            }
        }
        catch (Exception e)
        {
            log.error("库存占用失败!");
        }
        finally
        {
            //System.out.println(Thread.currentThread().getName() + "Redis释放锁。。。" + System.currentTimeMillis());
            fairLock.unlock();
        }
        return 0;
    }

    // 下单
    @Transactional(rollbackFor = Exception.class)
    public void addOrder(StockDemo stockDemo)
    {
        OrderDemo orderDemo = new OrderDemo();
        orderDemo.setOrderNo(stockDemo.getOrderNo());
        orderDemo.setProductNo(stockDemo.getProductNo());
        orderDemo.setStatus(1);
        orderDemo.setProductNum(stockDemo.getProductNum());
        orderDemo.setShopCode(stockDemo.getShopCode());
        orderDemoMapper.insert(orderDemo);
    }

    // 更新订单状态
    @Transactional(rollbackFor = Exception.class)
    public int updateOrderStatus(String orderNo, int status)
    {
        return orderDemoMapper.updateByOrderNo(orderNo, status);
    }

    /**
     * 释放库存
     *
     * @param stockDemo
     * @return
     */
    @Override
    public long releaseStock(StockDemo stockDemo)
    {
        if (checkStock(stockDemo, StockOptTypeEnum.RELEASE_STOCK.getOptType()))
        {
            return 0;
        }
        String productNo = stockDemo.getProductNo();
        String shopCode = stockDemo.getShopCode();
        SecureRandom secureRandom = null;
        try
        {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        // 公平锁
        final RLock fairLock = redissonClient.getFairLock(StockUtil.getLockPath(productNo, shopCode));
        // 异步 尝试加锁，最多等待50秒，上锁以后10秒自动解锁
        Future<Boolean> res = fairLock.tryLockAsync(50, 10, TimeUnit.SECONDS);
        try
        {
            //final boolean lock = fairLock.tryLock(50, 10, TimeUnit.SECONDS);
            if (res.get())
            {
                //System.out.println(Thread.currentThread().getName() + "Redis获得锁。。。" + System.currentTimeMillis());
                // 校验
                StockDemo stock = stockDemoMapper
                        .queryByProduct(stockDemo.getProductNo(), stockDemo.getShopCode(), secureRandom.nextInt(9999));
                if (null == stock)
                {
                    // 如果不存在直接抛异常扣库存失败
                    log.error("商品不存在库存信息：{}", stockDemo);
                    return 0;
                }
                StockDemo tempStock = null;
                boolean release = false;
                // 重试次数，5次更新机会
                while (null != stock)
                {
                    //System.out.println(Thread.currentThread().getName() + "查询的===========：" + stock);
                    int avaStock = stock.getAvaStock();
                    int preStock = stock.getPreStock();
                    int productNum = stockDemo.getProductNum();

                    // 可用+ 预占-
                    int newAvaStock = avaStock + productNum;
                    int newPreStock = preStock - productNum;

                    // 释放的库存必须是原有订单的库存数，也就是释放的库存不能为负数，否则就会出现数据异常
                    if (productNum > preStock)
                    {
                        log.error("释放库存异常，释放库存已经大与现有的预占库存，释放库存数据本身有问题");
                        throw new Exception("释放库存已经大与现有的预占库存!");
                    }

                    int totalStock = stock.getTotalStock();

                    // 预占+可用应该永远是=总库存的。即使总库存真实的减了，预占库存应该会对应的减
                    if (newAvaStock + newPreStock != totalStock)
                    {
                        //
                        log.error("库存释放异常，可用库存+预占库存与总库存数量不符合!总库存：{}，预占库存：{}，可用库存：{}", totalStock, newPreStock, newAvaStock);
                        throw new Exception("库存扣释放异常，可用库存+预占库存与总库存数量不符合");
                    }

                    tempStock = new StockDemo();
                    tempStock.setId(stock.getId());
                    // 旧库存  可用库存和预占库存
                    tempStock.setAvaStock(stock.getAvaStock());
                    tempStock.setPreStock(stock.getPreStock());
                    // 新库存 可用库存和预占库存
                    tempStock.setNewAvaStock(newAvaStock);
                    tempStock.setNewPreStock(newPreStock);

                    //System.out.println(Thread.currentThread().getName() + "更新的===========：" + tempStock);
                    //尝试CAS，先比较旧值，再更新
                    if (optStock(tempStock) > 0)
                    {
                        // 库存释放成功
                        log.info("库存释放成功，订单号：{},释放数量：{}", stockDemo.getOrderNo(), productNum);
                        release = true;
                        // 订单已完成
                        this.updateOrderStatus(stockDemo.getOrderNo(), 0);
                        break;
                    }
                    else
                    {
                        // 库存释放与预期的值不对，更新失败，尝试重新更新
                        log.info("库存释放更新时与预期的值不对，更新失败，尝试重新更新！");
                        //                                            tryPreTimes++;
                        stock = stockDemoMapper.queryByProduct(productNo, shopCode, secureRandom.nextInt(9999));
                    }
                }
                // 总库存
                tempStock.setTotalStock(stock.getTotalStock());
                tempStock.setNewTotalStock(stock.getTotalStock());

                // 预占成功处理日志
                return saveChangeLog(stockDemo, productNo, shopCode, stock, tempStock, release, StockOptTypeEnum.RELEASE_STOCK);
            }
            else
            {
                // 此处需要考虑下单失败后，补偿下单扣库存的
                log.error("库存释放失败，获取不到锁!");
                throw new Exception("库存释放失败，获取不到锁");
            }
        }
        catch (Exception e)
        {
            log.error("库存释放失败!");
        }
        finally
        {
            //System.out.println(Thread.currentThread().getName() + "释放锁。。。" + System.currentTimeMillis());
            fairLock.unlock();
        }
        return 0;
    }

    /**
     * 出库
     *
     * @param stockDemo
     * @return
     */
    @Override
    public long outHourse(StockDemo stockDemo)
    {
        if (checkStock(stockDemo, StockOptTypeEnum.OUT_WAREHOUSE_STOCK.getOptType()))
        {
            return 0;
        }
        String productNo = stockDemo.getProductNo();
        String shopCode = stockDemo.getShopCode();

        SecureRandom secureRandom = null;
        try
        {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        // 公平锁
        final RLock fairLock = redissonClient.getFairLock(StockUtil.getLockPath(productNo, shopCode));
        // 异步 尝试加锁，最多等待50秒，上锁以后10秒自动解锁
        Future<Boolean> res = fairLock.tryLockAsync(50, 10, TimeUnit.SECONDS);
        try
        {
            //            final boolean lock = fairLock.tryLock(50, 10, TimeUnit.SECONDS);
            if (res.get())
            {
                // 校验
                StockDemo stock = stockDemoMapper
                        .queryByProduct(stockDemo.getProductNo(), stockDemo.getShopCode(), secureRandom.nextInt(999));
                if (null == stock)
                {
                    // 如果不存在直接抛异常扣库存失败
                    log.error("商品不存在库存信息：{}", stockDemo);
                    return 0;
                }
                StockDemo tempStock = null;
                boolean outHourse = false;
                // 重试次数，5次更新机会
                int tryPreTimes = 0;
                //&& tryPreTimes < 5
                while (null != stock)
                {
                    //                    System.out.println("查询的===========：" + stock);
                    int preStock = stock.getPreStock();
                    int totalStock = stock.getTotalStock();
                    int productNum = stockDemo.getProductNum();

                    // 出库查库存应该是原来总库存中的数据，不能出现扣库存超数量情况
                    if (productNum > preStock || productNum > totalStock)
                    {
                        log.error("扣减实际库存异常，释放库存已经大与现有的预占库存或大与现有的总库存，扣减库存数据本身有问题");
                        throw new Exception("扣减实际库存大与现有的预占和总库存!");
                    }

                    //预占-，总库存-
                    int newPreStock = preStock - productNum;
                    int newTotalStock = totalStock - productNum;

                    int avaStock = stock.getAvaStock();
                    // 预占+可用应该永远是=总库存的。即使总库存真实的减了，预占库存应该会对应的减
                    if (avaStock + newPreStock != newTotalStock)
                    {
                        //
                        log.error("库存扣减异常，可用库存+预占库存与总库存数量不符合!总库存：{}，预占库存：{}，可用库存：{}", totalStock, newPreStock, avaStock);
                        throw new Exception("库存扣减放异常，可用库存+预占库存与总库存数量不符合");
                    }

                    tempStock = new StockDemo();
                    tempStock.setId(stock.getId());
                    // 旧库 总库存和预占库存
                    tempStock.setTotalStock(totalStock);
                    tempStock.setPreStock(stock.getPreStock());
                    // 新库存 总库存和预占库存
                    tempStock.setNewPreStock(newPreStock);
                    tempStock.setNewTotalStock(newTotalStock);

                    //                    tempStock.setAvaStock(stock.getAvaStock());
                    //                    System.out.println("更新的===========：" + tempStock);
                    //尝试CAS，先比较旧值，再更新
                    if (optStock(tempStock) > 0)
                    {
                        // 总库存减成功
                        log.info("总库存减成功，订单号：{}，减库存数量：{}", stockDemo.getOrderNo(), productNum);
                        outHourse = true;
                        // 订单已完成
                        this.updateOrderStatus(stockDemo.getOrderNo(), 2);
                        break;
                    }
                    else
                    {
                        // 库存释放与预期的值不对，更新失败，尝试重新更新
                        log.info("总库存更新与预期的值不对，库存出库扣减失败，尝试重新更新！");
                        //                        tryPreTimes++;
                        stock = stockDemoMapper.queryByProduct(productNo, shopCode, secureRandom.nextInt(9999));
                    }
                }
                // 可用库存
                tempStock.setAvaStock(stock.getAvaStock());
                tempStock.setNewAvaStock(stock.getAvaStock());

                // 预占成功处理日志
                return saveChangeLog(stockDemo, productNo, shopCode, stock, tempStock, outHourse, StockOptTypeEnum.OUT_WAREHOUSE_STOCK);
            }
            else
            {
                // 此处需要考虑下单失败后，补偿下单扣库存的
                log.error("库存出库扣减失败，获取不到锁!");
                throw new Exception("库存出库扣减失败，获取不到锁");
            }
        }
        catch (Exception e)
        {
            log.error("库存出库扣减失败!");
        }
        finally
        {
            fairLock.unlock();
        }
        return 0;
    }

    /**
     * [简要描述]:添加总库存<br/>
     * [详细描述]:<br/>
     * // 总库存+、可用库存+ 总之要保证：可用库存+预占库存=总库存
     *
     * @param stockDemo :
     * @return long
     * llxiao  2019/10/19 - 8:41
     **/
    @Override
    public long addStock(StockDemo stockDemo)
    {
        if (checkStock(stockDemo, StockOptTypeEnum.ADD_STOCK.getOptType()))
        {
            log.error("库存添加校验不通过!");
            return 0;
        }

        String productNo = stockDemo.getProductNo();
        String shopCode = stockDemo.getShopCode();

        SecureRandom secureRandom = null;
        try
        {
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        // 公平锁
        final RLock fairLock = redissonClient.getFairLock(StockUtil.getLockPath(productNo, shopCode));
        // 异步 尝试加锁，最多等待50秒，上锁以后10秒自动解锁
        Future<Boolean> res = fairLock.tryLockAsync(50, 10, TimeUnit.SECONDS);
        try
        {
            //final boolean lock = fairLock.tryLock(50, 10, TimeUnit.SECONDS);
            if (res.get())
            {
                // 校验
                StockDemo stock = stockDemoMapper
                        .queryByProduct(stockDemo.getProductNo(), stockDemo.getShopCode(), secureRandom.nextInt(999));
                if (null == stock)
                {
                    // 如果不存在直接抛异常扣库存失败
                    log.error("商品不存在库存信息：{}", stockDemo);
                    return 0;
                }
                StockDemo tempStock = null;
                boolean addStatus = false;
                // 重试次数，5次更新机会
                while (null != stock)
                {
                    int avaStock = stock.getAvaStock();
                    int totalStock = stock.getTotalStock();
                    int productNum = stockDemo.getProductNum();

                    //可用+，总库存+
                    int newAvaStock = avaStock + productNum;
                    int newTotalStock = totalStock + productNum;

                    int preStock = stock.getPreStock();
                    // 预占+可用应该永远是=总库存的。即使总库存真实的减了，预占库存应该会对应的减
                    if (newAvaStock + preStock != newTotalStock)
                    {
                        log.error("库存扣减异常，可用库存+预占库存与总库存数量不符合!总库存：{}，预占库存：{}，可用库存：{}", totalStock, preStock, newAvaStock);
                        throw new Exception("库存扣减放异常，可用库存+预占库存与总库存数量不符合");
                    }

                    tempStock = new StockDemo();
                    tempStock.setId(stock.getId());
                    // 旧库 总库存和预占库存
                    tempStock.setTotalStock(totalStock);
                    tempStock.setPreStock(stock.getPreStock());
                    tempStock.setAvaStock(avaStock);
                    // 新库存 总库存和预占库存
                    tempStock.setNewPreStock(stock.getPreStock());
                    tempStock.setNewTotalStock(newTotalStock);
                    tempStock.setNewAvaStock(newAvaStock);

                    //System.out.println("更新的===========：" + tempStock);
                    //尝试CAS，先比较旧值，再更新
                    if (optStock(tempStock) > 0)
                    {
                        // 总库存减成功
                        log.info("总库存添加成功功，添加数量：{}，总库存存数量：{}", productNum, newTotalStock);
                        addStatus = true;
                        break;
                    }
                    else
                    {
                        // 库存释放与预期的值不对，更新失败，尝试重新更新
                        log.info("总库存更新与预期的值不对，更新失败，尝试重新更新！");
                        //                        tryPreTimes++;
                        stock = stockDemoMapper.queryByProduct(productNo, shopCode, secureRandom.nextInt(9999));
                    }
                }

                // 预占成功处理日志
                return saveChangeLog(stockDemo, productNo, shopCode, stock, tempStock, addStatus, StockOptTypeEnum.OUT_WAREHOUSE_STOCK);
            }
            else
            {
                // 此处需要考虑下单失败后，补偿下单扣库存的
                log.error("库存释放失败，获取不到锁!");
                throw new Exception("库存释放失败，获取不到锁");
            }
        }
        catch (Exception e)
        {
            log.error("库存释放失败!错误消息：", e.getMessage());
        }
        finally
        {
            fairLock.unlock();
        }
        return 0;
    }

    /**
     * 获取订单信息
     *
     * @param orderNo
     * @return
     */
    @Override
    public OrderDemo getOrderByNo(String orderNo)
    {
        return orderDemoMapper.getOrderByNo(orderNo);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public int optStock(StockDemo updateStock)
    {
        return stockDemoMapper.preStock(updateStock);
    }

    private boolean checkStock(StockDemo stockDemo, String optType)
    {
        if (null == stockDemo || StringUtils.isBlank(stockDemo.getOrderNo()) || StringUtils
                .isBlank(stockDemo.getShopCode()) || StringUtils.isBlank(stockDemo.getProductNo()))
        {
            // 参数为直接抛异常扣库存失败
            log.error("请求参数为空：{}", stockDemo);
            return true;
        }

        StockChangeLodDemo stockChangeLodDemo = stockChangeLodDemoMapper
                .selectByOrderNo(stockDemo.getOrderNo(), stockDemo.getProductNo(), optType);
        if (null != stockChangeLodDemo)
        {
            log.error("预占库存失败，库存日志记录已经存在，订单号：{}，商品SKU：{}", stockDemo.getOrderNo(), stockDemo.getProductNo());
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public long saveChangeLog(StockDemo stockDemo, String productNo, String shopCode, StockDemo stock,
            StockDemo tempStock, boolean release, StockOptTypeEnum optTypeEnum) throws Exception
    {
        if (release)
        {
            tempStock.setProductNo(stockDemo.getProductNo());
            tempStock.setShopCode(stockDemo.getShopCode());
            tempStock.setOrderNo(stockDemo.getOrderNo());
            return saveStockChangeLog(tempStock, optTypeEnum);
        }
        else
        {
            log.error("下单失败，可用库存不足，单号：{}，店铺：{}", stockDemo.getOrderNo(), shopCode);
            log.error("商品：{}，预占库存：{}", productNo, stockDemo.getProductNum());
            throw new Exception("下单失败，库存不足!");
        }
    }

    private Long saveStockChangeLog(StockDemo stockDemo, StockOptTypeEnum stockOptTypeEnum)
    {
        StockChangeLodDemo stockChangeLodDemo = new StockChangeLodDemo();
        stockChangeLodDemo.setOrderNo(stockDemo.getOrderNo());
        stockChangeLodDemo.setProductNo(stockDemo.getProductNo());
        stockChangeLodDemo.setShopCode(stockDemo.getShopCode());
        stockChangeLodDemo.setAvaStockBefore(stockDemo.getAvaStock());
        stockChangeLodDemo.setPreStockBefore(stockDemo.getPreStock());
        stockChangeLodDemo.setTotalStockBefore(stockDemo.getTotalStock());
        stockChangeLodDemo.setAvaStockAfter(stockDemo.getNewAvaStock());
        stockChangeLodDemo.setPreStockAfter(stockDemo.getNewPreStock());
        stockChangeLodDemo.setTotalStockAfter(stockDemo.getNewTotalStock());
        stockChangeLodDemo.setOptType(stockOptTypeEnum.getOptType());
        //        System.out.println("保存日志：" + stockChangeLodDemo);
        stockChangeLodDemoMapper.insert(stockChangeLodDemo);
        return stockChangeLodDemo.getId();
    }
}
