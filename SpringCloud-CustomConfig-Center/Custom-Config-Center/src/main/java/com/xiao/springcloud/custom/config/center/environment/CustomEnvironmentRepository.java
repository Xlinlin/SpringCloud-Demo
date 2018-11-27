package com.xiao.springcloud.custom.config.center.environment;

import com.xiao.springcloud.custom.config.center.service.RepositoryService;
import org.apache.commons.collections.MapUtils;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * [简要描述]: 数据库实现
 * [详细描述]:
 *
 * @author llxiao
 * @version 1.0, 2018/11/22 15:16
 * @since JDK 1.8
 */
public class CustomEnvironmentRepository implements EnvironmentRepository, Ordered
{
    private int order = Ordered.LOWEST_PRECEDENCE - 10;

    private RepositoryService repositoryService;

    public CustomEnvironmentRepository(RepositoryService repositoryService)
    {
        this.repositoryService = repositoryService;
    }

    @Override
    public Environment findOne(String application, String profile, String label)
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        String ip = "";
        if (null != servletRequestAttributes)
        {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            //获取IP
            ip = getClientIp(request);
        }

        String config = application;
        if (StringUtils.isEmpty(label))
        {
            label = "master";
        }
        if (StringUtils.isEmpty(profile))
        {
            profile = "default";
        }
        if (!profile.startsWith("default"))
        {
            profile = "default," + profile;
        }
        String[] profiles = StringUtils.commaDelimitedListToStringArray(profile);
        Environment environment = new Environment(application, profiles, label, null, null);
        if (!config.startsWith("application"))
        {
            config = "application," + config;
        }
        List<String> applications = new ArrayList<>(new LinkedHashSet<>(Arrays
                .asList(StringUtils.commaDelimitedListToStringArray(config))));
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
                    environment.add(new PropertySource(app + "-" + env, resource));
                }
            }
        }
        return environment;
    }

    private String getClientIp(HttpServletRequest request)
    {
        String ip = null;
        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
        {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
        {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
        {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
        {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0)
        {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses))
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
