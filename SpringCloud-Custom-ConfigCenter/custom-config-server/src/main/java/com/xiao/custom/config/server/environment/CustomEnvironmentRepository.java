package com.xiao.custom.config.server.environment;

import com.xiao.custom.config.server.manager.ClientManagerService;
import com.xiao.custom.config.server.service.RepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springframework.util.StringUtils.commaDelimitedListToStringArray;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * [简要描述]: 数据库实现
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/22 15:16
 * @since JDK 1.8
 */
@Slf4j
public class CustomEnvironmentRepository implements EnvironmentRepository, Ordered
{
    /**
     * config server 一些默认配置
     */
    private static final String APPLICATION = "application";
    private static final String DEFAULT_PROFILE = "default";
    private static final String DEFAULT_LABEL = "master";

    private static final String CLIENT_HOST = "ClientServerHost";
    private static final String CLIENT_PORT = "ClientServerPort";

    /**
     * Squid 服务代理
     */
    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    /**
     * apache 服务代理
     */
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    /**
     * weblogic 服务代理
     */
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    /**
     * http 其他代理
     */
    private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
    /**
     * nginx服务代理
     */
    private static final String X_REA_IP = "X-Real-IP";
    private static final String UNKNOWN = "unknown";

    private int order = Ordered.LOWEST_PRECEDENCE - 10;

    private RepositoryService repositoryService;

    private ClientManagerService clientManagerService;

    public CustomEnvironmentRepository(RepositoryService repositoryService, ClientManagerService clientManagerService)
    {
        this.repositoryService = repositoryService;
        this.clientManagerService = clientManagerService;
    }

    @Override
    public Environment findOne(String application, String profile, String label)
    {
        String ip = saveClientInfo(application, profile);

        String config = application;
        if (isEmpty(label))
        {
            label = DEFAULT_LABEL;
        }
        if (isEmpty(profile))
        {
            profile = DEFAULT_PROFILE;
        }
        if (!profile.startsWith(DEFAULT_PROFILE))
        {
            profile = DEFAULT_PROFILE + ',' + profile;
        }
        String[] profiles = commaDelimitedListToStringArray(profile);
        Environment environment = new Environment(application, profiles, label, null, null);
        if (!config.startsWith(APPLICATION))
        {
            config = APPLICATION + ',' + config;
        }
        List<String> applications = new ArrayList<>(new LinkedHashSet<>(Arrays
                .asList(commaDelimitedListToStringArray(config))));
        List<String> envs = new ArrayList<>(new LinkedHashSet<>(Arrays.asList(profiles)));
        Collections.reverse(applications);
        Collections.reverse(envs);
        Map<String, Object> resource;
        for (String app : applications)
        {
            for (String env : envs)
            {
                resource = repositoryService.getPropertySource(ip, app, label, env);
                if (MapUtils.isNotEmpty(resource))
                {
                    environment.add(new PropertySource(app + '-' + env, resource));
                }
            }
        }
        return environment;
    }

    private String saveClientInfo(String application, String profile)
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        String ip = "";
        String port = "";
        if (null != servletRequestAttributes)
        {
            HttpServletRequest request = servletRequestAttributes.getRequest();

            //获取IP
            ip = request.getHeader(CLIENT_HOST);
            if (org.apache.commons.lang3.StringUtils.isBlank(ip))
            {
                ip = getClientIp(request);
            }
            port = request.getHeader(CLIENT_PORT);
            log.info(">>> 应用：{}开始从配置中心拉取配置", application);
            log.info(">>> 应用端服务器信息-IP:{},Port:{}", ip, port);
            if (!isEmpty(ip) && !isEmpty(port))
            {
                clientManagerService.setClientHost(application, profile, ip, Integer.parseInt(port));
            }
        }
        return ip;
    }

    private String getClientIp(HttpServletRequest request)
    {
        String ip = null;
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader(X_FORWARDED_FOR);
        if (isEmpty(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader(PROXY_CLIENT_IP);
        }
        if (isEmpty(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (isEmpty(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader(HTTP_CLIENT_IP);
        }
        if (isEmpty(ipAddresses) || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader(X_REA_IP);
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0)
        {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @Override
    public int getOrder()
    {
        return order;
    }
}
