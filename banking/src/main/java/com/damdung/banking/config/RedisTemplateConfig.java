package com.damdung.banking.config;

import jakarta.validation.Valid;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {
    @Value("${redis.idempotency.host}")
    private String idempotencyHost;

    @Value("${redis.idempotency.port}")
    private int idempotencyPort;

    @Value("${redis.idempotency.password}")
    private String idempotencyPassword;

    @Bean(name = "idempotencyRedisConnectionFactory")
    public LettuceConnectionFactory idempotencyRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
                idempotencyHost
                , idempotencyPort);
        if(idempotencyPassword != null && !idempotencyPassword.isEmpty()) {
            redisStandaloneConfiguration.setPassword(idempotencyPassword);
        }
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "idempotencyTemplate")
    public RedisTemplate<String,String> idempotencyTemplate(
            @Qualifier("idempotencyRedisConnectionFactory") LettuceConnectionFactory factory
            )
    {
        RedisTemplate<String,String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();

        return template;
    }
}
