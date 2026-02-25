package com.damdung.banking.controller;

import com.damdung.banking.model.dto.CreateBankAccountDTO;
import com.damdung.banking.model.dto.CreateTransactionsDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.security.utils.SecurityUtils;
import com.damdung.banking.service.IBankService;
import com.damdung.banking.service.IdempotencyService;
import com.damdung.banking.service.impl.validate.TransferValidate;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class BankAccountController {
    @Autowired
    private IBankService bankService;
    @Autowired
    private IdempotencyService idempotencyService;
    @Autowired
    private TransferValidate transferValidate;

    public ResponseEntity<Object> responseLimitCreateTransaction(@RequestHeader("Idempotency-Key") String key
            , @RequestBody CreateTransactionsDTO createTransactionsDTO, Throwable t) {
        return ResponseEntity.status(429).body(Map.of("message", "Too many requests!"));
    }

    public ResponseEntity<Object> responseLimitCreateFormBankAccount(@RequestBody CreateBankAccountDTO bankAccountDTO, Throwable t) {
        return ResponseEntity.status(429).body(Map.of("message", "Too many requests!"));
    }

    /**
     * name đùng để config resilience4j trong properties
     * fallbackMethod là gọi 1 method để fall lỗi cho client
     * */
    @RateLimiter(name = "spam", fallbackMethod = "responseLimitCreateFormBankAccount")
    @PostMapping("/bank-accounts")
    public ResponseEntity<Object> createBankAccounts(@RequestBody CreateBankAccountDTO bankAccountDTO) {
        MyUserDetail myUserDetail = SecurityUtils.getPrincipal();
        return bankService.createBankAccount(myUserDetail, bankAccountDTO);
    }
    
    /**
     * Chuyển tiền
     * */
    @RateLimiter(name = "spam", fallbackMethod = "responseLimitCreateTransaction")
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransactions(@RequestHeader("Idempotency-Key") String key
            , @RequestBody CreateTransactionsDTO createTransactionsDTO) {
        // Xử lý Double-charge
        MyUserDetail myUserDetail = SecurityUtils.getPrincipal();

        Map<String, Object> object = transferValidate.isValidate(myUserDetail, createTransactionsDTO);

        Boolean accquire = idempotencyService.tryAcquire(key);
        
        if(!accquire) {
            String oldResponse = idempotencyService.getOldResponse(key);
            if(oldResponse != null) {
                return ResponseEntity.ok(oldResponse);
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Request is already processing!"));
        }

        return bankService.createTransactions(object , createTransactionsDTO, key);
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
