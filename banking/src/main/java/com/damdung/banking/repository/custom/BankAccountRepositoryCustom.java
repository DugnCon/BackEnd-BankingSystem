package com.damdung.banking.repository.custom;

import com.damdung.banking.model.dto.GetBankAccountDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import jakarta.persistence.Tuple;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface BankAccountRepositoryCustom {
    Map<String, Object> isAccountExists(MyUserDetail myUserDetail);
    GetBankAccountDTO getAccounts(MyUserDetail myUserDetail);
}
