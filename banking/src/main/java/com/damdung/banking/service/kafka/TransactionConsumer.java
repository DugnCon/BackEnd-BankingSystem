package com.damdung.banking.service.kafka;

import com.damdung.banking.annotation.LoggingAnnotation;
import com.damdung.banking.entity.account.TransactionEntity;
import com.damdung.banking.entity.account.TransferEntity;
import com.damdung.banking.entity.account.TransferHistoryEntity;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.repository.ITransferHistoryRepository;
import com.damdung.banking.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class TransactionConsumer {
    @Autowired
    private JavaMailSender mail;
    @Autowired
    private ITransferHistoryRepository transferHistoryRepository;

    @KafkaListener(topics = "transactions", groupId = "transaction-email")
    public void sendEmail(Map<String,Object> message, Acknowledgment ack) {

        ack.acknowledge();
    }

    @KafkaListener(topics = "transactions", groupId = "transaction-history")
    public void saveTransactionHistory(Map<String, Object> message, Acknowledgment ack) {
        ack.acknowledge();
    }

    @KafkaListener(topics = "transactions", groupId = "transaction-audit")
    public void logTransaction(Map<String, Object> message, Acknowledgment ack) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("Transaction audit logged successfully by: {}", auth.getDetails());

        ack.acknowledge();
    }
}
