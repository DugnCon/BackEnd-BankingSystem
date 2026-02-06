package com.damdung.banking.model.dto;

import com.damdung.banking.annotation.StrongPasswordAnnotation;
import com.damdung.banking.annotation.group.Create;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AuthRegisterDTO {
    @NotBlank(groups = Create.class, message = "Không được để trống tên")
    private String firstName;
    @NotBlank(groups = Create.class, message = "Không được để trống tên")
    private String lastName;
    private String address1;
    private String city;
    private String state;
    private String postalCode;
    @Past(groups = Create.class, message = "Sai kiểu dữ liệu ngày tháng năm sinh")
    private LocalDate dateOfBirth;
    private String ssn;
    @Email(groups = Create.class, message = "Lỗi Email")
    private String email;
    @Length(groups = Create.class, min = 8, max = 20, message = "Mật khẩu không đủ độ dài")
    @StrongPasswordAnnotation(groups = Create.class)
    private String password;
}
