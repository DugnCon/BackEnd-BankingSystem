package com.damdung.banking.service;

import com.damdung.banking.model.dto.CreateBankAccountDTO;
import com.damdung.banking.model.dto.CreateTransactionsDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface IBankService {
    ResponseEntity<Object> createBankAccount(MyUserDetail myUserDetail, CreateBankAccountDTO bankAccountDTO);
    ResponseEntity<Object> isAccountExists(MyUserDetail myUserDetail);
    ResponseEntity<Object> getAccounts(MyUserDetail myUserDetail);
    ResponseEntity<Object> getTransactionHistory(MyUserDetail myUserDetail, Integer limit, Long accountID);
    ResponseEntity<Object> createTransactions(Map<String, Object> object, CreateTransactionsDTO createTransactionsDTO,String key);
}
