package com.damdung.banking.config.kafka;

import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.acks}")
    private String acks;

    @Value("${spring.kafka.producer.retries}")
    private Integer retries;

    @Value("${spring.kafka.producer.compression-type}")
    private String compressionType;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        // Liệt kê hết các broker và Kafka client sẽ tự động failover
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Serializers
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // Cấu hình để tăng khả năng chịu lỗi và scale
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);// Đợi tất cả replicas
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);// Thử lại khi lỗi
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType); // Nén dữ liệu

        // Tối ưu hiệu suất cho nhiều partition
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);// 16KB batch
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);// Đợi 5ms để gom batch
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);// 32MB buffer

        // Tăng timeout cho cluster nhiều broker
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);

        // Idempotent producer, tránh duplicate khi retry
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}