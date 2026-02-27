package com.damdung.banking.service.impl;

import com.damdung.banking.annotation.LoggingAnnotation;
import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.model.dto.QrTransactionDTO;
import com.damdung.banking.repository.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@LoggingAnnotation
public class QrTransactionImpl {
    @Autowired
    private IBankAccountRepository bankAccountRepository;
    public QrTransactionDTO getQrTransactionDTO(MyUserDetail myUserDetail) {
        try {
            Long userID = myUserDetail.getUserID();
            BankAccountEntity bankAccountEntity = bankAccountRepository.findByAuth_UserID(userID);

            QrTransactionDTO qrTransactionDTO = new QrTransactionDTO();
            qrTransactionDTO.setAccountNumber(bankAccountEntity.getAccountNumber());
            qrTransactionDTO.setAccountName(bankAccountEntity.getOfficialName());
            qrTransactionDTO.setBankName("UETBank");
            qrTransactionDTO.setExpiresIn(LocalTime.ofSecondOfDay(5000));

            return qrTransactionDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
