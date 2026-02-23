package com.damdung.banking.service.impl;

import com.damdung.banking.annotation.LoggingAnnotation;
import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.entity.auth.AuthEntity;
import com.damdung.banking.model.dto.CreateBankAccountDTO;
import com.damdung.banking.model.dto.GetBankAccountDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.repository.IBankAccountRepository;
import com.damdung.banking.repository.custom.BankAccountRepositoryCustom;
import com.damdung.banking.service.IBankService;
import com.damdung.banking.utils.MapUtils;
import com.damdung.banking.utils.SQLUtils;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Slf4j
@Service
@LoggingAnnotation
public class BankServiceImpl implements IBankService {
    @Autowired
    private IBankAccountRepository bankAccountRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SQLUtils sqlUtils;
    @Autowired
    private BankAccountRepositoryCustom bankAccountRepositoryCustom;

    /**
     * Tạo tài khoản account
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> createBankAccount(MyUserDetail myUserDetail, CreateBankAccountDTO bankAccountDTO) {
        AuthEntity auth = sqlUtils.findAuth(myUserDetail.getUserID());
        BankAccountEntity bankAccount = modelMapper.map(bankAccountDTO, BankAccountEntity.class);

        bankAccount.setAuth(auth);

        bankAccountRepository.save(bankAccount);

        String accountNumber = "9704" +
                String.format("%010d", bankAccount.getAccountID());

        bankAccount.setAccountNumber(accountNumber);
        bankAccount.setShareableID(accountNumber);
        bankAccount.setMask(accountNumber.substring(10));

        return ResponseEntity.ok(Map.of("message", "Account created successfully", "success", true));
    }

    /**
     * Kiểm tra tồn tại tài khoản
     * */
    @Override
    public ResponseEntity<Object> isAccountExists(MyUserDetail myUserDetail) {
        Map<String, Object> result = bankAccountRepositoryCustom.isAccountExists(myUserDetail);

        Long hasAccounts = MapUtils.getObject(result, "hasAccounts", Long.class);

        if(hasAccounts > 0) {
            return ResponseEntity.ok(Map.of("hasAccounts", true, "totalBanks", hasAccounts));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("hasAccounts", false, "totalBanks", 0));
    }

    /**
     * Lấy dữ liệu accounts của người dùng
     * */
    @Override
    public ResponseEntity<Object> getAccounts(MyUserDetail myUserDetail) {
        GetBankAccountDTO bankAccountDTO = bankAccountRepositoryCustom.getAccounts(myUserDetail);
        BigDecimal currentBalance = bankAccountDTO.currentBalance();

        Map<String, Object> result = bankAccountRepositoryCustom.isAccountExists(myUserDetail);
        Long totalBanks = MapUtils.getObject(result, "hasAccounts", Long.class);

        assert totalBanks != null;
        return ResponseEntity.ok(Map.of("data", bankAccountDTO, "totalBanks", totalBanks, "totalCurrentBalance", currentBalance));
    }
}
