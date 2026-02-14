package com.damdung.banking.model.dto;

import com.damdung.banking.annotation.group.Create;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AuthInformationDTO {
    private Long userID;
    private String firstName;
    private String lastName;
    private String address1;
    private String city;
    private String state;
    private String postalCode;
    private LocalDate dateOfBirth;
    private String ssn;
    private String email;

    public AuthInformationDTO() {}

    public AuthInformationDTO(Long userID, String firstName, String lastName, String email) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
