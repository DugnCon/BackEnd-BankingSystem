package com.damdung.banking.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EMAIL_TRANSACTION_MESSAGE_QUEUE = "email_transaction_queue";
    public static final String EMAIL_SIGN_UP_QUEUE = "email_sign_up_queue";

    public static final String EMAIL_RETRY_QUEUE = "email_retry_queue";
    public static final String EMAIL_DLQ = "email_dlq";

    public static final String EMAIL_EXCHANGE = "email_exchange";
    public static final String EMAIL_DLX = "email_dlx";

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public DirectExchange emailDeadLetterExchange() {
        return new DirectExchange(EMAIL_DLX);
    }

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder.durable(EMAIL_TRANSACTION_MESSAGE_QUEUE)
                .withArgument("x-dead-letter-exchange", EMAIL_DLX)
                .withArgument("x-dead-letter-routing-key", "retry")
                .build();
    }

    @Bean
    public Queue signupQueue() {
        return QueueBuilder.durable(EMAIL_SIGN_UP_QUEUE)
                .withArgument("x-dead-letter-exchange", EMAIL_DLX)
                .withArgument("x-dead-letter-routing-key", "retry")
                .build();
    }

    @Bean
    public Queue retryQueue() {
        return QueueBuilder.durable(EMAIL_RETRY_QUEUE)
                .withArgument("x-message-ttl", 5000)
                .withArgument("x-dead-letter-exchange", EMAIL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "main")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(EMAIL_DLQ).build();
    }

    @Bean
    public Binding transactionBinding() {
        return BindingBuilder.bind(transactionQueue())
                .to(emailExchange())
                .with("transaction");
    }

    @Bean
    public Binding signupBinding() {
        return BindingBuilder.bind(signupQueue())
                .to(emailExchange())
                .with("signup");
    }

    @Bean
    public Binding retryBinding() {
        return BindingBuilder.bind(retryQueue())
                .to(emailDeadLetterExchange())
                .with("retry");
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(emailDeadLetterExchange())
                .with("dlq");
    }
}