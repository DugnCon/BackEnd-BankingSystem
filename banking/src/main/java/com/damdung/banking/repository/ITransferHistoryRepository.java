package com.damdung.banking.repository;

import com.damdung.banking.entity.account.TransferHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransferHistoryRepository extends JpaRepository<TransferHistoryEntity, Long> {
}
