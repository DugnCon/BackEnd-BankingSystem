package com.damdung.banking.repository;

import com.damdung.banking.entity.account.TransferHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransferHistoryRepository extends JpaRepository<TransferHistoryEntity, Long> {
    List<TransferHistoryEntity> findBySender_AccountID(Long accountID, Pageable pageable);
}
