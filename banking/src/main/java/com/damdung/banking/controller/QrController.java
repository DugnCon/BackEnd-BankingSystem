package com.damdung.banking.controller;

import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.security.utils.SecurityUtils;
import com.damdung.banking.service.QrTransactionService;
import com.damdung.banking.service.impl.QrTransactionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class QrController {
    @Autowired
    private QrTransactionService qrTransactionService;
    @Autowired
    private QrTransactionImpl qrTransactionImpl;
    @PostMapping(produces = MediaType.IMAGE_PNG_VALUE, path = "/qr/my-qr")
    public byte[] generateQrCode() throws Exception {
        MyUserDetail myUserDetail = SecurityUtils.getPrincipal();
        return qrTransactionService.generateQrCode(qrTransactionImpl.getQrTransactionDTO(myUserDetail));
    }
    @PostMapping(path = "/qr/decode")
    public ResponseEntity<Object> decodeQr(@RequestBody Map<String, String> qr) {
        String emv = qr.get("qrData");
        return qrTransactionService.decodeQr(emv);
    }
}
