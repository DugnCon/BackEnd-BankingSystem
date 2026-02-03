package com.damdung.banking.annotation;

import com.damdung.banking.annotation.validator.StrongPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPasswordAnnotation {
    String message() default "Mật khẩu quá yếu";
    Class<?>[] groups() default {}; // Phải có khi có @Constraint
    Class<? extends Payload>[] payload() default {}; // Phải có khi có @Constraint
}
