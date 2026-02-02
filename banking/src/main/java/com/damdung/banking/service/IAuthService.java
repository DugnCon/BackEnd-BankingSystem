package com.damdung.banking.service;

import com.damdung.banking.model.dto.AuthLoginDTO;
import com.damdung.banking.model.dto.AuthRegisterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface IAuthService {
    ResponseEntity<Object> authRegister(AuthRegisterDTO authRegisterDTO);
    ResponseEntity<Object> authLogin(AuthLoginDTO authLoginDTO);
}
