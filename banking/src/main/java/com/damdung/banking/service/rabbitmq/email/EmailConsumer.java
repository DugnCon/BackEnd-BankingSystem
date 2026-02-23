package com.damdung.banking.service.rabbitmq.email;

import com.damdung.banking.config.rabbitmq.RabbitMQConfig;
import com.damdung.banking.utils.MapUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailConsumer {
    @Autowired
    private JavaMailSender mailSender;
    @RabbitListener(queues = RabbitMQConfig.EMAIL_MESSAGE_QUEUE)
    public void receiveEmailUpload(Map<String,Object> message) throws JsonProcessingException {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(MapUtils.getObject(message,"toEmail", String.class));
        email.setSubject(MapUtils.getObject(message,"toSubject", String.class));
        email.setText(MapUtils.getObject(message, "toText", String.class));

        mailSender.send(email);
    }
}
