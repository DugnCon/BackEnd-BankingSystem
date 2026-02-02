package com.damdung.banking.controller;

import com.damdung.banking.annotation.group.Create;
import com.damdung.banking.model.dto.AuthLoginDTO;
import com.damdung.banking.model.dto.AuthRegisterDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IAuthService iAuthService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Object> register(@Validated(Create.class) @RequestBody AuthRegisterDTO authRegisterDTO) {
        return iAuthService.authRegister(authRegisterDTO);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<Object> login(@Valid @RequestBody AuthLoginDTO authLoginDTO) {
        return null;
    }
}
