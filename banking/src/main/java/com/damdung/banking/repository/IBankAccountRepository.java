package com.damdung.banking.repository;

import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.model.dto.GetBankAccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
}
