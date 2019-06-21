package com.xiao.springcloud.demo.common.cache.conf;

import com.xiao.springcloud.demo.common.cache.code.FastJsonCodec;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@Configuration
@Slf4j
// 属性值存在该配置才会生效
@ConditionalOnProperty(name = RedissonConfig.REDISSON_FILE_NAME)
public class RedissonConfig
{
    public static final String REDISSON_FILE_NAME = "redisson.fileName";

    @Value("${spring.cloud.config.uri:}")
    private String configUrl;
    @Value("${spring.cloud.config.label:}")
    private String label;
    @Value("${spring.cloud.config.profile:}")
    private String profile;
    /* redissoin配置文件名称 */
    @Value("${redisson.fileName:redission}")
    private String redissionFileName;

    // 配置中心读取ression.yml文件
    private String getYamlFromConfig() throws IOException
    {
        String uri = configUrl + label + "/" + redissionFileName + "-" + profile + ".yml";
        log.info("redission配置文件uri:" + uri);
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();
        StringBuilder buffer = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream())))
        {
            String msg = null;
            while ((msg = br.readLine()) != null)
            {
                buffer.append(msg).append("\r\n");
            }
        }
        return buffer.toString();
    }

    @Bean
    public Config config() throws IOException
    {

        Config config = Config.fromYAML(getYamlFromConfig());
        if (config.isClusterConfig())
        {
            config.useClusterServers().setLoadBalancer(new RoundRobinLoadBalancer());
        }

        return config;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient(Config config) throws IOException
    {
        log.info("create RedissonClient, config is : {}", config.toJSON());

        //序列化统一使用fastjson
        config.setCodec(FastJsonCodec.INSTANCE);
        return Redisson.create(config);
    }

}
