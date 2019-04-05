package com.xiao.custom.config.server.manager.impl;

import com.xiao.custom.config.server.manager.ClientManagerService;
import com.xiao.custom.config.server.manager.SqlConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * [简要描述]: 客户端管理jdbc实现
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2019/1/29 16:28
 * @since JDK 1.8
 */
@Service
@Slf4j
public class ClientManagerServiceDbImpl implements ClientManagerService, SqlConstants
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    /**
     * [简要描述]:存储服务链接信息，服务IP<br/>
     * [详细描述]:<br/>
     *
     * @param serviceName : 应用名
     * @param profile : 应用环境
     * @param hostIp : 应用对应服务的IP
     * @param hostPort : 应用对应服务的端口
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setClientHost(String serviceName, String profile, String hostIp, int hostPort)
    {
        if (log.isDebugEnabled())
        {
            log.debug("=======存储服务链接信息:");
            log.debug("应用名称:{}", serviceName);
            log.debug("应用环境:{}", profile);
            log.debug("应用IP:{}", hostIp);
            log.debug("应用端口:{}", hostPort);
        }
        // 应用
        Long id = processApplication(serviceName, profile);
        // 应用主机信息
        processHost(hostIp, hostPort, id);
    }

    /**
     * [简要描述]:更新状态<br/>
     * [详细描述]:<br/>
     *
     * @param hostIp : 客户端IP
     * @param hostPort : 客户端PORT
     * @param status : status
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String hostIp, int hostPort, int status)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("nettyIp", hostIp);
        params.put("nettyPort", hostPort);
        namedParameterJdbcTemplate.update(SqlConstants.UPDATE_CLIENT_STATUS, params);

    }

    /**
     * [简要描述]:<br/>
     * [详细描述]:<br/>
     *
     * @param hostIp :
     * @param hostPort :
     * @param nettyPort :
     * @param nettyHostIp llxiao  2019/4/1 - 11:49
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNettyInfo(String hostIp, int hostPort, int nettyPort, String nettyHostIp)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("nettyPort", nettyPort);
        params.put("ip", hostIp);
        params.put("port", hostPort);
        params.put("nettyIp", nettyHostIp);
        namedParameterJdbcTemplate.update(SqlConstants.UPDATE_NETTY_INFO, params);
    }

    /**
     * 客户端应用维护
     *
     * @param serviceName
     * @param profile
     * @return
     */
    private Long processApplication(String serviceName, String profile)
    {
        //应用+环境 是否存在
        Map<String, Object> params = new HashMap<>();
        params.put("application", serviceName);
        params.put("profile", profile);
        Long id = getPriKey(SELECT_APPLICATION, params);

        if (null == id)
        {
            //插入
            id = insertApplication(serviceName, profile);
        }
        else
        {
            updateApplicationStatus(id);
        }
        return id;
    }

    private Long getPriKey(String sql, Map<String, Object> params)
    {
        Long id = null;
        try
        {
            id = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        }
        catch (EmptyResultDataAccessException e)
        {
            log.warn("获取不到数据");
        }
        return id;
    }

    /**
     * 客户主机信息维护
     *
     * @param hostIp
     * @param hostPort
     * @param id
     */
    private void processHost(String hostIp, int hostPort, Long id)
    {
        // 添加客户端IP和端口信息
        Map<String, Object> params = new HashMap<>(3);
        params.put("appId", id);
        params.put("ip", hostIp);
        params.put("port", hostPort);
        Long hostId = getPriKey(SELECT_APPLICATION_HOST_INFO, params);
        if (null == hostId)
        {
            //添加
            namedParameterJdbcTemplate.update(INSERT_APPLICATION_HOST_INFO, params);
        }
        else
        {
            params = new HashMap<>(1);
            params.put("id", id);
            //更新状态
            namedParameterJdbcTemplate.update(UPDATE_APPLICATION_HOST_INFO, params);
        }
    }

    private void updateApplicationStatus(Long id)
    {
        Map<String, Long> param = new HashMap<>(1);
        param.put("id", id);
        namedParameterJdbcTemplate.update(UPDATE_APPLICATION, param);
    }

    private Long insertApplication(String serviceName, String profile)
    {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("application", serviceName);
        parameters.addValue("profile", profile);
        return insertReKey(INSERT_APPLICATION, parameters);
        //        return insertReId(connection ->
        //        {
        //            PreparedStatement pstmt = connection.prepareStatement(INSERT_APPLICATION, Statement.RETURN_GENERATED_KEYS);
        //            pstmt.setString(1, serviceName);
        //            pstmt.setString(2, profile);
        //            return pstmt;
        //        });
    }

    /**
     * [简要描述]:jdbcTemplate方式插入一条数据，返回主键<br/>
     * [详细描述]:<br/>
     *
     * @param preparedStatementCreator :
     * @return java.lang.Long
     * llxiao  2019/1/29 - 17:11
     **/
    private Long insertReId(PreparedStatementCreator preparedStatementCreator)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * [简要描述]:NamedParameterJdbcTemplate方式插入一条数据，返回主键<br/>
     * [详细描述]:<br/>
     *
     * @param sql : 执行的SQL
     * @param params : 参数
     * @return java.lang.Long
     * llxiao  2019/1/29 - 17:44
     **/
    private Long insertReKey(String sql, SqlParameterSource params)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[] { "id" });
        return keyHolder.getKey().longValue();
    }
}
