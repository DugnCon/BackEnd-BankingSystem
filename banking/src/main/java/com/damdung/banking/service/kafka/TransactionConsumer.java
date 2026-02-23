package com.damdung.banking.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionConsumer {

    @KafkaListener(topics = "test", groupId = "test")
    public void listen(String message, Acknowledgment ack) {
        log.info("Message: {}", message);

        // Commit rồi mới xong
        ack.acknowledge();
    }
}
