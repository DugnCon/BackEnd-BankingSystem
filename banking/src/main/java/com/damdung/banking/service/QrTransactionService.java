package com.damdung.banking.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@Service
public class QrTransactionService {
    @Autowired
    private ObjectMapper objectMapper;

    public byte[] generateQrCode(Object data) throws Exception {
        String text = objectMapper.writeValueAsString(data);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 400, 400);

        BufferedImage bufferedImage = new BufferedImage(400, 400, BufferedImage.TYPE_INT_BGR);

        for(int x = 0; x < 400 ; x++) {
            for(int y = 0; y < 400; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
            }
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}
