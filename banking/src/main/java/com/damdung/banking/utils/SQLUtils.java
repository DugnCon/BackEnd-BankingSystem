package com.damdung.banking.utils;

import com.damdung.banking.entity.auth.AuthEntity;
import com.damdung.banking.repository.IAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SQLUtils {
    @Autowired
    private IAuthRepository authRepository;

    public AuthEntity findAuth(Long userID) {
        return authRepository.findById(userID).orElseThrow(() -> new RuntimeException("Not found user"));
    }
}
