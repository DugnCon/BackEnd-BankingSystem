package com.damdung.banking.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BandwidthBuilder;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Cònfig số lượng giới hạn key trong bucket
 * */
@Configuration
public class RateLimitConfig {
    @Bean
    public BucketConfiguration bucketConfiguration() {
        Bandwidth limit = Bandwidth.builder()
                .capacity(5)
                .refillGreedy(5, Duration.ofMinutes(5))
                .build();

        return BucketConfiguration.builder()
                .addLimit(limit)
                .build();
    }
}
