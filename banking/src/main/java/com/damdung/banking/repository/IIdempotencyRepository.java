package com.damdung.banking.repository;

import com.damdung.banking.entity.IdempotencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IIdempotencyRepository extends JpaRepository<IdempotencyEntity, String> {
}
