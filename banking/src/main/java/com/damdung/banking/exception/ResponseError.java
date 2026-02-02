package com.damdung.banking.exception;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

public class ResponseError {
    private String message;
    private List<String> errors;
    public ResponseError(String message) {
        this.message = message;
    }
    public ResponseError(String message, List<String> errors) {
        this.message = message;
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
