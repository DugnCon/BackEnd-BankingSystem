package com.damdung.banking.model.dto;

import java.math.BigDecimal;

public record GetBankAccountDTO(
        Long accountID,
        Long userID,
        String accountNumber,
        String officialName,
        String shareableID,
        String name,
        String type,
        String mask,
        BigDecimal availableBalance,
        BigDecimal currentBalance,
        String currency
) {}