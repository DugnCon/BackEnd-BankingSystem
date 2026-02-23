package com.damdung.banking.service.rabbitmq.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSignupService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail(String toEmail) {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(toEmail);
        email.setSubject("Signup Success");
        email.setText("Your account has been created successfully!");

        mailSender.send(email);
    }
}
