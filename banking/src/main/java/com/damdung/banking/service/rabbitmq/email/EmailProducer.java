package com.damdung.banking.service.rabbitmq.email;

import com.damdung.banking.config.rabbitmq.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class EmailProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void setUploadTask(String toEmail, String toSubject, String toText) {
        Map<String,Object> message = new HashMap<>();
        message.put("toEmail", toEmail);
        message.put("toSubject", toSubject);
        message.put("toText", toText);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EMAIL_MESSAGE_QUEUE, message);
    }
}
