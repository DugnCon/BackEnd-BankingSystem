package com.damdung.banking.repository;

import com.damdung.banking.entity.account.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransferRepository extends JpaRepository<TransferEntity, Long> {
}
