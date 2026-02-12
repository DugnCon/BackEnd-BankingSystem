package com.damdung.banking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthInformationDTO {
    private Long userID;
    private String firstName;
    private String lastName;
    private String email;

    public AuthInformationDTO() {}
}
