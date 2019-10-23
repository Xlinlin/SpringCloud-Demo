package com.xiao.stock.demo.configure;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration
{

    @Value(value = "${hikari-jdbc-url}")
    private String hikariJdbcUrl = "";
    @Value(value = "${hikari-jdbc-password}")
    private String hikariJdbcPassword;
    @Value(value = "${hikari-jdbc-username}")
    private String hikariJdbcUsername;
    @Value(value = "${hikari-jdbc-driver-class-name}")
    private String hikariJdbcDriverClassName;
    @Value(value = "${hikari-jdbc-pool-size}")
    private int hikariJdbcPoolSize;

    // Hikari 连接池
    @Bean(name = "dataSource")
    public DataSource dataSource()
    {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(hikariJdbcUrl);
        ds.setUsername(hikariJdbcUsername);
        ds.setPassword(hikariJdbcPassword);
        ds.setDriverClassName(hikariJdbcDriverClassName);
        ds.setMaximumPoolSize(hikariJdbcPoolSize);
        ds.setConnectionInitSql("set names utf8mb4;");
        return ds;
    }
}
