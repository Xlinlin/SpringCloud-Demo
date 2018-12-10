package com.xiao.springcloud.job.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class JobConfig
{

    @Autowired
    private TaskSchedulerFactory taskSchedulerFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean()
    {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setJobFactory(taskSchedulerFactory);
        return bean;
    }

    @Bean
    public Scheduler scheduler()
    {
        return schedulerFactoryBean().getScheduler();
    }

//    @Bean
//    public HttpClient httpClient()
//    {
//        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
//        return httpClientBuilder.build();
//    }
//
//    @Bean
//    public ClientHttpRequestFactory clientHttpRequestFactory()
//    {
//        return new HttpComponentsClientHttpRequestFactory(httpClient());
//    }
}
