package com.damdung.banking.controller;

import com.damdung.banking.annotation.group.Create;
import com.damdung.banking.model.dto.AuthLoginDTO;
import com.damdung.banking.model.dto.AuthRegisterDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.security.utils.SecurityUtils;
import com.damdung.banking.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Validated(Create.class) @RequestBody AuthRegisterDTO authRegisterDTO) {
        return authService.authRegister(authRegisterDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthLoginDTO authLoginDTO) {
        return authService.authLogin(authLoginDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout() {
        return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công"));
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getInformation() {
        MyUserDetail myUserDetail = SecurityUtils.getPrincipal();
        return authService.getInformation(myUserDetail);
    }
}
