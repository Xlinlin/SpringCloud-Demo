package com.xiao.springcloud.job.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * 自定义的 TaskSchedulerFactory
 * @Date: 2018/11/2 16:08
 * @Description:
 */
@Component
public class TaskSchedulerFactory extends AdaptableJobFactory
{

    /**
     * 需要使用这个BeanFactory对Qurartz创建好Job实例进行后续处理.
     */
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception
    {
        // 首先，调用父类的方法创建好Quartz所需的Job实例
        Object jobInstance = super.createJobInstance(bundle);
        // 然后，使用BeanFactory为创建好的Job实例进行属性自动装配并将其纳入到Spring容器的管理之中，属于Spring的技术范畴.
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

}
