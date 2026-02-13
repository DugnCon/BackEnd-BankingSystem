package com.damdung.banking.config.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String EMAIL_MESSAGE_QUEUE = "email_message_queue";

    @Bean
    public Queue mediaUploadQueue() {
        return new Queue(EMAIL_MESSAGE_QUEUE, true);
    }
}
