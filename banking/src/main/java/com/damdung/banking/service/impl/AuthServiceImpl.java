package com.damdung.banking.service.impl;

import com.damdung.banking.annotation.LoggingAnnotation;
import com.damdung.banking.entity.auth.AuthEntity;
import com.damdung.banking.model.dto.AuthLoginDTO;
import com.damdung.banking.model.dto.AuthRegisterDTO;
import com.damdung.banking.repository.IAuthRepository;
import com.damdung.banking.service.IAuthService;
import com.damdung.banking.service.JWTService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@LoggingAnnotation
public class AuthServiceImpl implements IAuthService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwt;
    @Autowired
    private IAuthRepository authRepository;
    /**
     * Lưu tài khoản cho người dùng
     * */
    @Override
    @Transactional
    public ResponseEntity<Object> authRegister(AuthRegisterDTO authRegisterDTO) {
        String newPassword = passwordEncoder.encode(authRegisterDTO.getPassword());
        AuthEntity authEntity = modelMapper.map(authRegisterDTO, AuthEntity.class);
        authEntity.setRole("CUSTOMER");
        authEntity.setAddress(authRegisterDTO.getAddress1());

        authEntity.setPassword(newPassword);

        authRepository.save(authEntity);

        return ResponseEntity.ok(Map.of("message", "Tạo tài khoản thành công"));
    }
    /**
     * Xác nhận đăng nhập cho người dùng
     * */
    @Override
    public ResponseEntity<Object> authLogin(AuthLoginDTO authLoginDTO) {
        AuthEntity authEntity = authRepository.findByEmail(authLoginDTO.getEmail());

        if(authEntity == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Sai tài khoản và mật khẩu"));
        else if(!passwordEncoder.matches(authLoginDTO.getPassword(), authEntity.getPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Sai tài khoản và mật khẩu"));

        Map<String,Object> claims = new HashMap<>();
        claims.put("email", authEntity.getEmail());
        claims.put("userID", authEntity.getUserID());
        claims.put("role", authEntity.getRole());

        String token = jwt.getTokenWithClaims(claims);
        return ResponseEntity.ok(Map.of("message", "Đăng nhập thành công",
                                        "token", token));
    }
}
