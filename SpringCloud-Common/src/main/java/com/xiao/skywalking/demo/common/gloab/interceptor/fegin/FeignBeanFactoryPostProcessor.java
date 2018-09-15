package com.xiao.skywalking.demo.common.gloab.interceptor.fegin;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * [简要描述]: 解决junit测试完毕销毁FeignContext 异常问题
 * [详细描述]: 异常信息：BeanCreationNotAllowedException: Error creating bean with name 'eurekaAutoServic'....
 * git 参考地址：https://github.com/spring-cloud/spring-cloud-netflix/issues/1952
 *
 * @author llxiao
 * @version 1.0, 2018/9/11 15:19
 * @since JDK 1.8
 */
@Component
public class FeignBeanFactoryPostProcessor implements BeanFactoryPostProcessor
{
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        if (containsBeanDefinition(beanFactory, "feignContext", "eurekaAutoServiceRegistration"))
        {
            BeanDefinition bd = beanFactory.getBeanDefinition("feignContext");
            bd.setDependsOn("eurekaAutoServiceRegistration");
        }
    }

    private boolean containsBeanDefinition(ConfigurableListableBeanFactory beanFactory, String... beans)
    {
        return Arrays.stream(beans).allMatch(b -> beanFactory.containsBeanDefinition(b));
    }
}
