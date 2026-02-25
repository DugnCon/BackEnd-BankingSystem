package com.damdung.banking.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateTransactionsDTO {
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private String note;
    private String email;
}
