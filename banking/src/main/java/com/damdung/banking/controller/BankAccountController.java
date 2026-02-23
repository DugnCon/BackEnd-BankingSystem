package com.damdung.banking.controller;

import com.damdung.banking.model.dto.CreateBankAccountDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.security.utils.SecurityUtils;
import com.damdung.banking.service.IBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class BankAccountController {
    @Autowired
    private IBankService bankService;

    @PostMapping("/bank-accounts")
    public ResponseEntity<Object> createBankAccounts(@RequestBody CreateBankAccountDTO bankAccountDTO) {
        MyUserDetail myUserDetail = SecurityUtils.getPrincipal();
        return bankService.createBankAccount(myUserDetail, bankAccountDTO);
    }

    @GetMapping("/my/accounts")
    public ResponseEntity<Object> getAccounts() {
        MyUserDetail myUserDetail = SecurityUtils.getPrincipal();
        return bankService.getAccounts(myUserDetail);
    }

    @GetMapping("/my/accounts/check")
    public ResponseEntity<Object> isAccountExists() {
        MyUserDetail myUserDetail = SecurityUtils.getPrincipal();
        return bankService.isAccountExists(myUserDetail);
    }
}
