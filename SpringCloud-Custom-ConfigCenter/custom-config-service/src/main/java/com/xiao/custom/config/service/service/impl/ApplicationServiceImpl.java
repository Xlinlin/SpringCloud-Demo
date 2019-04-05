package com.xiao.custom.config.service.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiao.custom.config.pojo.dto.ApplicationConfigDto;
import com.xiao.custom.config.pojo.dto.ApplicationDto;
import com.xiao.custom.config.pojo.entity.Application;
import com.xiao.custom.config.pojo.entity.ApplicationConfig;
import com.xiao.custom.config.pojo.entity.ClientHostInfo;
import com.xiao.custom.config.pojo.mapper.*;
import com.xiao.custom.config.pojo.query.AppQuery;
import com.xiao.custom.config.pojo.query.ApplicationConfigQuery;
import com.xiao.custom.config.service.feign.RefreshFeign;
import com.xiao.custom.config.service.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * [简要描述]: 应用服务
 * [详细描述]:
 *
 * @author jun.liu
 * @version 1.0, 2018/11/26 16:47
 * @since JDK 1.8
 */
@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService
{
    /**
     * 刷新配置接口常量
     */
    private static final String REFRESH_URL = "/config/refresh";
    private static final int SUCCESS = 0;
    private static final int HTTP_SUCCESS_CODE = 200;
    private static final int SERVICE_DOWN = 1;

    @Autowired
    private ApplicationMapper applicationMapper;
    @Autowired
    private ApplicationItemGroupRelationMapper applicationItemGroupRelationMapper;

    @Autowired
    private ApplicationConfigMapper applicationConfigMapper;

    @Autowired
    private ClientHostInfoMapper clientHostInfoMapper;

    @Autowired
    private ClientApplicationMapper clientApplicationMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RefreshFeign refreshFeign;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer save(Application applicationConfig, String[] groupIdArr)
    {
        int a = applicationMapper.insert(applicationConfig);
        if (groupIdArr != null && groupIdArr.length > 0 && a > 0)
        {
            Long appId = applicationConfig.getId();
            return applicationItemGroupRelationMapper.batchSave(groupIdArr, appId);
        }
        return a;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(Application applicationConfig)
    {
        return applicationMapper.updateByPrimaryKey(applicationConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Long id)
    {
        int delFlag = applicationMapper.deleteByPrimaryKey(id);
        if (delFlag > 0)
        {
            //删除关联应用的配置组信息
            applicationItemGroupRelationMapper.deleteByAppId(id);
        }
        return delFlag;
    }

    @Override
    public PageInfo<ApplicationDto> pageApplicationConfig(AppQuery appQuery, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(applicationMapper.pageApplicationConfig(appQuery));
    }

    @Override
    public Application selectApplicationConfigById(Long id)
    {
        return applicationMapper.selectByPrimaryKey(id);
    }

    /**
     * [简要描述]:分页查询应用关联的私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param query :
     * @return java.util.List<com.winner.config.center.pojo.db.dto.ApplicationConfigDto>
     * llxiao  2019/1/7 - 15:27
     **/
    @Override
    public PageInfo<ApplicationConfigDto> pageQuery(ApplicationConfigQuery query)
    {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<ApplicationConfigDto> applicationConfigDtos = new ArrayList<>();
        if (null != query && null != query.getApplicationId())
        {
            applicationConfigDtos = applicationConfigMapper.pageQuery(query);
        }
        return new PageInfo<>(applicationConfigDtos);
    }

    /**
     * [简要描述]:保存应用的私有配置信息<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfig :
     * @return boolean
     * llxiao  2019/1/7 - 16:56
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveApplicationConfig(ApplicationConfig applicationConfig)
    {
        int flag = 0;
        if (null != applicationConfig)
        {
            flag = this.applicationConfigMapper.insert(applicationConfig);
        }
        return flag > 0;
    }

    /**
     * [简要描述]:主键更新<br/>
     * [详细描述]:<br/>
     *
     * @param applicationConfig :
     * @return boolean
     * llxiao  2019/1/7 - 16:58
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApplicationConfig(ApplicationConfig applicationConfig)
    {
        int flag = 0;
        if (null != applicationConfig && null != applicationConfig.getId())
        {
            flag = this.applicationConfigMapper.updateByPrimaryKeySelective(applicationConfig);
        }
        return flag > 0;
    }

    /**
     * [简要描述]:删除私有属性<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return boolean
     * llxiao  2019/1/8 - 9:21
     **/
    @Override
    public boolean delPrivateConfig(Long id)
    {
        if (null != id)
        {
            return this.applicationConfigMapper.deleteByPrimaryKey(id) > 0;
        }
        return false;
    }

    /**
     * [简要描述]:发布配置<br/>
     * [详细描述]:<br/>
     *
     * @param id :
     * @return boolean
     * llxiao  2019/1/30 - 10:53
     **/
    @Override
    public boolean refresh(Long id)
    {
        Application application = this.applicationMapper.selectByPrimaryKey(id);
        if (null != application)
        {
            //应用+环境查找 已连接上的应用
            List<ClientHostInfo> hostInfos = clientHostInfoMapper
                    .queryByApplication(application.getApplication(), application.getProfile());
            int size = 0;
            if (CollectionUtil.isNotEmpty(hostInfos))
            {
                for (ClientHostInfo hostInfo : hostInfos)
                {
                    if (refreshFeign.refresh(hostInfo.getNettyIp(), hostInfo.getNettyPort()))
                    {
                        size++;
                    }
                    //                    if (restRefresh(hostInfo))
                    //                    {
                    //                        size++;
                    //                    }
                }

                //所有更新服务失败，需要标记应用下线
                if (size == 0)
                {
                    clientApplicationMapper
                            .updateStatus(application.getApplication(), application.getProfile(), SERVICE_DOWN);
                }

                return size > 0;
            }
        }
        return false;
    }

    /**
     * [简要描述]:指定客户端按户型配置<br/>
     * [详细描述]:<br/>
     *
     * @param hostInfoIds : 客户端信息
     * @return boolean
     * llxiao  2019/3/27 - 14:06
     **/
    @Override
    public boolean batchRefresh(Long... hostInfoIds)
    {
        int size = 0;
        if (null != hostInfoIds)
        {
            ClientHostInfo hostInfo;
            for (Long hostInfoId : hostInfoIds)
            {
                hostInfo = this.clientHostInfoMapper.selectByPrimaryKey(hostInfoId);
                //在线状态
                if (null != hostInfo && hostInfo.getStatus() == ClientHostInfo.ONLINE)
                {
                    if (refreshFeign.refresh(hostInfo.getNettyIp(), hostInfo.getNettyPort()))
                    {
                        size++;
                    }
                    //                    if (restRefresh(hostInfo))
                    //                    {
                    //                        size++;
                    //                    }
                }
            }
        }
        return size > 0;
    }

    //    /**
    //     * [简要描述]:远程发起刷新请求<br/>
    //     * [详细描述]:<br/>
    //     *
    //     * @param hostInfo :
    //     * llxiao  2019/1/30 - 11:30
    //     **/
    //    private boolean restRefresh(ClientHostInfo hostInfo)
    //    {
    //        String url = "http://" + hostInfo.getHostIp() + ':' + hostInfo.getHostPort() + REFRESH_URL;
    //        // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
    //        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
    //        // 消息头
    //        HttpHeaders headers = new HttpHeaders();
    //        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(paramMap, headers);
    //        boolean flag = true;
    //        int status = 0;
    //        try
    //        {
    //            ResponseEntity<Integer> result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Integer.class);
    //            status = result.getStatusCodeValue();
    //            if (HTTP_SUCCESS_CODE == status && SUCCESS == result.getBody())
    //            {
    //                log.info("应用更新成功：应用服务IP:{},应用服务PORT:{}", hostInfo.getHostIp(), hostInfo.getHostPort());
    //            }
    //            else
    //            {
    //                log.error("应用更新失败，服务端返回状态:{}，返回更新结果：{}", status, result.getBody());
    //                flag = false;
    //            }
    //        }
    //        catch (Exception e)
    //        {
    //            log.error("服务错误!", e.getMessage());
    //        }
    //        if (flag)
    //        {
    //            log.error("应用发布失败：应用服务IP:{},应用服务PORT:{}", hostInfo.getHostIp(), hostInfo.getHostPort());
    //            log.error("服务返回HTTP状态：{}", status);
    //            //标记服务已经下线
    //            clientHostInfoMapper.updateStatus(hostInfo.getId(), SERVICE_DOWN);
    //        }
    //        return flag;
    //    }
}
