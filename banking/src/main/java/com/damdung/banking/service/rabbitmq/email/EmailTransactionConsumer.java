package com.damdung.banking.service.rabbitmq.email;

import com.damdung.banking.config.rabbitmq.RabbitMQConfig;
import com.damdung.banking.model.dto.event.TransferEventDTO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailTransactionConsumer {

    @Autowired
    private JavaMailSender mail;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_TRANSACTION_MESSAGE_QUEUE)
    public void receiveMessage(TransferEventDTO dto,
                               Message message,
                               Channel channel) throws IOException {

        long tag = message.getMessageProperties().getDeliveryTag();

        try {

            SimpleMailMessage senderMail = new SimpleMailMessage();
            SimpleMailMessage receiverMail = new SimpleMailMessage();

            senderMail.setTo(dto.getSenderEmail());
            senderMail.setSubject("Transfer Successfully!");
            senderMail.setText("You have successfully transfered to "
                    + dto.getReceiverAccountNumber()
                    + "\nThe amount transferred is "
                    + dto.getFee());

            receiverMail.setTo(dto.getReceiverEmail());
            receiverMail.setSubject("Transfer Successfully!");
            receiverMail.setText("You have received the money transfer from "
                    + dto.getReceiverAccountNumber()
                    + "\nThe amount you received is "
                    + dto.getFee());

            mail.send(senderMail, receiverMail);

            channel.basicAck(tag, false);

        } catch (Exception ex) {

            Integer retry = (Integer) message.getMessageProperties()
                    .getHeaders()
                    .getOrDefault("x-retry-count", 0);

            if (retry >= 3) {

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EMAIL_DLX,
                        "dlq",
                        dto
                );

            } else {

                message.getMessageProperties()
                        .getHeaders()
                        .put("x-retry-count", retry + 1);

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.EMAIL_DLX,
                        "retry",
                        dto,
                        m -> {
                            m.getMessageProperties().getHeaders()
                                    .put("x-retry-count", retry + 1);
                            return m;
                        }
                );
            }

            channel.basicAck(tag, false);
        }
    }
}