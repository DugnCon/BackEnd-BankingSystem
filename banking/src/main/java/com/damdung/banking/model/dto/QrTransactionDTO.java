package com.damdung.banking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QrTransactionDTO {
    private String receiverId;
    private String accountNumber;
    private String accountName;
    private String bankName;
    private LocalTime expiresIn;
}
