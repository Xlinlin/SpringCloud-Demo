package com.purcotton.sharding.sphere.demo;

import com.purcotton.sharding.sphere.demo.service.CommonService;
import com.purcotton.sharding.sphere.demo.service.impl.SpringPojoServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/14 11:23
 * @since JDK 1.8
 */
@MapperScan(basePackages = "com.purcotton.sharding.sphere.demo.repository")
@SpringBootApplication
public class SpringBootStarterExample
{
    public static void main(final String[] args)
    {
        try (ConfigurableApplicationContext applicationContext = SpringApplication
                .run(SpringBootStarterExample.class, args))
        {
            process(applicationContext);
        }
    }

    private static void process(final ConfigurableApplicationContext applicationContext)
    {
        CommonService commonService = getCommonService(applicationContext);
//        commonService.initEnvironment();
        commonService.processSuccess(false);
//        try
//        {
//            commonService.processFailure();
//        }
//        catch (final Exception ex)
//        {
//            System.out.println(ex.getMessage());
//            commonService.printData(false);
//        }
//        finally
//        {
//            commonService.cleanEnvironment();
//        }
    }

    private static CommonService getCommonService(final ConfigurableApplicationContext applicationContext)
    {
        return applicationContext.getBean(SpringPojoServiceImpl.class);
    }
}
