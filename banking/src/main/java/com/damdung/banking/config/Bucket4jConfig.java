package com.damdung.banking.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kết nối giữa Bucket4j với Redis
 * */
@Configuration
public class Bucket4jConfig {

    @Bean
    public ProxyManager<String> proxyManager(
            StatefulRedisConnection<String, byte[]> redisConnection
    ) {
        return LettuceBasedProxyManager.builderFor(redisConnection)
                .build();
    }
}
