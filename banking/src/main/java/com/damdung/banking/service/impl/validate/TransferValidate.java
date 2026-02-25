package com.damdung.banking.service.impl.validate;

import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.entity.account.TransactionEntity;
import com.damdung.banking.entity.account.TransferEntity;
import com.damdung.banking.exception.BusinessException;
import com.damdung.banking.model.dto.CreateTransactionsDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.repository.IBankAccountRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TransferValidate {
    @Autowired
    private IBankAccountRepository bankAccountRepository;

    @Transactional
    public Map<String,Object> isValidate(MyUserDetail myUserDetail, CreateTransactionsDTO createTransactionsDTO) {
        Long senderID = myUserDetail.getUserID();
        String receiverNumberCard = createTransactionsDTO.getReceiverId();

        BankAccountEntity sender  = bankAccountRepository.findByAuth_UserID(senderID);
        BankAccountEntity receiver  = bankAccountRepository.findByAccountNumber(receiverNumberCard);

        if(!sender.getAccountNumber().equals(receiverNumberCard) && sender.getAuth().getEmail().equals(receiver.getAuth().getEmail())) {
            throw new BusinessException("Money transfer formatting error!");
        }

        if (sender == null) {
            throw new ResourceNotFoundException("Not found sender!");
        }

        if (receiver == null) {
            throw new ResourceNotFoundException("Not found receiver!");
        }

        BigDecimal amount = createTransactionsDTO.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Incorrect currency format. Please try again!");
        }

        if (amount.compareTo(sender.getAvailableBalance()) > 0) {
            throw new BusinessException("The amount is greater than the available funds. Please repeat the process!");
        }

        if (amount.compareTo(BigDecimal.valueOf(100000000)) > 0) {
            throw new BusinessException("The amount is higher than the allowed limit. Please try again!");
        }
        return Map.of("sender", sender, "receiver", receiver);
    }
}
