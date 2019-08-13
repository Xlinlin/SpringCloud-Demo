package com.xiao.hystrix.demo.consumer.dynamic;

import com.netflix.config.PollResult;
import com.netflix.config.PolledConfigurationSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * [简要描述]: 动态刷新hystrix配置
 * [详细描述]: 可以从DB、redis、zookeeper、配置中心等等获取配置文件
 *
 * @author llxiao
 * @version 1.0, 2019/8/5 18:53
 * @since JDK 1.8
 */
@Component
@ConditionalOnProperty(name = "hystrix.config.application")
@Slf4j
public class DynamicConfigSource implements PolledConfigurationSource
{

    public static final String HYSTRIX_CONFIG_APPLICATION = "hystrix.config.application";

    @Value("${hystrix.config.application:hystrix}")
    private String application;
    @Value("${spring.cloud.config.profile:}")
    private String profile;
    @Value("${spring.cloud.config.uri:}")
    private String uri;
    @Value("${spring.cloud.config.label:}")
    private String label;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Poll the configuration source to get the latest content.
     *
     * @param initial true if this operation is the first poll.
     * @param checkPoint Object that is used to determine the starting point if the result returned is incremental.
     * Null if there is no check point or the caller wishes to get the full content.
     * @return The content of the configuration which may be full or incremental.
     * @exception Exception If any exception occurs when fetching the configurations.
     */
    @Override
    public PollResult poll(boolean initial, Object checkPoint) throws Exception
    {
        // 获取配置
        return PollResult.createFull(getFromConfigCenter());
    }

    /**
     * [简要描述]:配置中心获取hystrix配置<br/>
     * [详细描述]:<br/>
     *
     * @return java.util.Map
     * llxiao  2019/8/8 - 8:49
     **/
    private Map<String, Object> getFromConfigCenter()
    {
        Map<String, Object> complete = new HashMap<>();
        Environment environment = getRemoteEnvironment();
        if (null != environment)
        {
            if (environment.getPropertySources() != null)
            {
                for (PropertySource source : environment.getPropertySources())
                {
                    complete.putAll((Map<String, Object>) source.getSource());
                }
            }
        }
        return complete;
    }

    private Environment getRemoteEnvironment()
    {
        String path = "/{name}/{profile}";

        Object[] args = new String[] { application, profile };
        if (StringUtils.hasText(label))
        {
            args = new String[] { application, profile, label };
            path = path + "/{label}";
        }
        ResponseEntity<Environment> response = null;

        try
        {
            HttpHeaders headers = new HttpHeaders();
            final HttpEntity<Void> entity = new HttpEntity<>((Void) null, headers);
            response = restTemplate.exchange(uri + path, HttpMethod.GET, entity, Environment.class, args);
        }
        catch (HttpClientErrorException e)
        {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND)
            {
                throw e;
            }
        }

        if (response == null || response.getStatusCode() != HttpStatus.OK)
        {
            return null;
        }
        Environment result = response.getBody();
        return result;
    }
}
