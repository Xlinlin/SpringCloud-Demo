package com.xiao.stock.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * [简要描述]: 库存扣减测试
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/10/17 09:23
 * @since JDK 1.8
 */
@SpringBootApplication(scanBasePackages = "com.purcotton.stock.demo", exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.purcotton.stock.demo.mapper")
public class StockDemoApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(StockDemoApplication.class, args);
    }
}
