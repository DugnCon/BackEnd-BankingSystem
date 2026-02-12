package com.damdung.banking.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisClientConfig {

    @Bean
    public RedisClient redisClient() {
        return RedisClient.create("redis://localhost:6666");
    }

    @Bean
    public StatefulRedisConnection<String, byte[]> redisConnection(
            RedisClient redisClient
    ) {
        return redisClient.connect(
                RedisCodec.of(
                        io.lettuce.core.codec.StringCodec.UTF8,
                        io.lettuce.core.codec.ByteArrayCodec.INSTANCE
                )
        );
    }
}
