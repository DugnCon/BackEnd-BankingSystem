package com.damdung.banking.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserInfor(@PathVariable Long userId) {
        return null;
    }
    @GetMapping("/{userId}/accounts")
    public ResponseEntity<Object> getAccounts(@PathVariable String userId) {
        Long userID = Long.parseLong(userId);
        return null;
    }
}
