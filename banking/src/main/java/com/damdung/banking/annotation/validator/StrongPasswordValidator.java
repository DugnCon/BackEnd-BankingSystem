package com.damdung.banking.annotation.validator;

import com.damdung.banking.annotation.StrongPasswordAnnotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class StrongPasswordValidator implements ConstraintValidator<StrongPasswordAnnotation, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraint) {
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9-].*");
        return (hasLower && hasUpper && hasDigit && hasSpecial);
    }
}
