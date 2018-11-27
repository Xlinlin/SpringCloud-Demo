package com.xiao.springcloud.custom.config.center.config;

import com.xiao.springcloud.custom.config.center.environment.CustomEnvironmentRepository;
import com.xiao.springcloud.custom.config.center.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [简要描述]:
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/22 15:49
 * @since JDK 1.8
 */
@Configuration
public class CustomEnvironmentRepositoryConfiguration
{
    @Autowired
    private RepositoryService repositoryService;

    @Bean
    public EnvironmentRepository environmentRepository()
    {
        return new CustomEnvironmentRepository(repositoryService);
    }

}
