package com.damdung.banking.model.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthLoginDTO {
    @Email(message = "Lỗi định dạng")
    private String email;
    private String password;
}
