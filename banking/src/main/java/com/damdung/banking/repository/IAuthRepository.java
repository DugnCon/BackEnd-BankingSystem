package com.damdung.banking.repository;

import com.damdung.banking.entity.auth.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAuthRepository extends JpaRepository<AuthEntity, Long> {
    AuthEntity findByEmail(String email);
    AuthEntity findByEmailAndPassword(String email, String password);
}
