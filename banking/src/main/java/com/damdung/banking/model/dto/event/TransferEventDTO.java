package com.damdung.banking.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferEventDTO {
    private String senderEmail;
    private String receiverEmail;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private BigDecimal fee;
}
