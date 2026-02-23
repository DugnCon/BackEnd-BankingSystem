package com.damdung.banking.model.dto;

import com.damdung.banking.annotation.group.Create;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateBankAccountDTO {
    @NotBlank(groups = Create.class,message = "Không được để trống")
    private String name;
    private String officialName;
    @NotBlank(groups = Create.class, message = "Không được để trống")
    private String subtype;
    @NotBlank(groups = Create.class, message = "Không được để trống")
    private String mask;
    //private String institutionId;
    private BigDecimal availableBalance;
    private BigDecimal currentBalance;
}
