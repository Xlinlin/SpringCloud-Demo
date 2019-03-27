package com.xiao.springcloud.disruptor.service.impl;

import com.xiao.springcloud.disruptor.TableData;
import com.xiao.springcloud.disruptor.service.DisruptorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * [简要描述]: 异步队列服务
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/3/25 09:51
 * @since JDK 1.8
 */
@Service
@Slf4j
public class DisruptorServiceImpl implements DisruptorService, ApplicationListener<ContextRefreshedEvent>
{

    /**
     * [简要描述]:异步处理服务<br/>
     * [详细描述]:<br/>
     *
     * @param tableData :
     * @return void
     * llxiao  2019/3/25 - 9:51
     **/
    @Override
    public void execute(TableData tableData)
    {
        if (log.isDebugEnabled())
        {
            log.debug("接受数据更新请求，更新表名:{},更新的主键为:{}", tableData.getTableName(), tableData.getId());
        }
        if (null != tableData)
        {
            //业务处理 TODO
        }
    }

    /**
     * 初始化所有的service服务
     */
    private void initAllCacheService()
    {
        log.info(">>> 初始化所有需要缓存的服务对象.....");
        //业务需要初始化一些处理
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent)
    {
        initAllCacheService();
    }
}
