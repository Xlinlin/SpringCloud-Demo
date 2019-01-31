package com.xiao.custom.config.server.config;

import com.xiao.custom.config.server.environment.CustomEnvironmentRepository;
import com.xiao.custom.config.server.manager.ClientManagerService;
import com.xiao.custom.config.server.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

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

    @Autowired
    private ClientManagerService clientManagerService;

    @Bean
    public EnvironmentRepository environmentRepository()
    {
        return new CustomEnvironmentRepository(repositoryService, clientManagerService);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(JdbcTemplate template)
    {
        return new NamedParameterJdbcTemplate(template);
    }

}
