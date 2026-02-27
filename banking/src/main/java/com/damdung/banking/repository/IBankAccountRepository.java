package com.damdung.banking.repository;

import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.entity.auth.AuthEntity;
import com.damdung.banking.model.dto.GetBankAccountDTO;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
public interface IBankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    BankAccountEntity findByAuth_UserID(Long userID);
    BankAccountEntity findByAccountNumber(String shareableID);
}
