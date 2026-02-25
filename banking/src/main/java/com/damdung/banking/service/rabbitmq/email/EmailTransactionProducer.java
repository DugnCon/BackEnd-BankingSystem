package com.damdung.banking.service.rabbitmq.email;

import com.damdung.banking.config.rabbitmq.RabbitMQConfig;
import com.damdung.banking.model.dto.event.TransferEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailTransactionProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendMessage(TransferEventDTO transferEventDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_TRANSACTION_MESSAGE_QUEUE, transferEventDTO);
    }
}
