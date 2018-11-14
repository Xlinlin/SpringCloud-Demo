package com.purcotton.sharding.sphere.demo.service;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/14 11:14
 * @since JDK 1.8
 */
public interface CommonService
{
    void initEnvironment();
    void cleanEnvironment();
    void processSuccess(boolean isRangeSharding);
    void processFailure();
    void printData(boolean isRangeSharding);

}
