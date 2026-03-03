package com.damdung.banking.service;

import com.damdung.banking.model.dto.QrTransactionDTO;
import com.damdung.banking.utils.QrUtils.EmvBuilder;
import com.damdung.banking.utils.QrUtils.EmvNode;
import com.damdung.banking.utils.QrUtils.EmvParser;
import com.damdung.banking.utils.QrUtils.TlvBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

@Service
public class QrTransactionService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EmvParser parser;
    public byte[] generateQrCode(QrTransactionDTO dto) throws Exception {
        String merchantInfo = new TlvBuilder()
                .add("00", "A000000727")
                .add("01", dto.getBankName())
                .add("02", dto.getAccountNumber())
                .add("03", dto.getAccountName())
                .build();

        String additionalData = new TlvBuilder()
                .add("08", dto.getReceiverId())
                .build();

        String emv = new EmvBuilder()
                .add("00", "01")
                .add("01", "12")
                .add("38", merchantInfo)
                .add("53", "704")
                .add("58", "VN")
                .add("62", additionalData)
                .build();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(
                emv,
                BarcodeFormat.QR_CODE,
                400,
                400
        );

        BufferedImage bufferedImage =
                new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < 400; x++) {
            for (int y = 0; y < 400; y++) {
                bufferedImage.setRGB(
                        x,
                        y,
                        bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF
                );
            }
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    public ResponseEntity<Object> decodeQr(String  emv) {
        EmvNode root = parser.parse(emv);

        EmvNode merchant = root.get("38");

        String bankName = merchant.get("01").getValue();
        String accountNumber = merchant.get("02").getValue();
        String accountName = merchant.get("03").getValue();

        EmvNode additional = root.get("62");
        String receiverId = additional.get("08").getValue();

        QrTransactionDTO transactionDTO = new QrTransactionDTO();

        transactionDTO.setBankName(bankName);
        transactionDTO.setAccountNumber(accountNumber);
        transactionDTO.setReceiverId(receiverId);
        transactionDTO.setAccountName(accountName);

        return ResponseEntity.ok(Map.of("success", true, "data", transactionDTO));
    }

//    public <T> T decodeQr(InputStream inputStream, Class<T> clazz) throws Exception{
//        BufferedImage bufferedImage = ImageIO.read(inputStream);
//
//        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
//        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
//
//        Result rs = new MultiFormatReader().decode(bitmap);
//
//        String text = rs.getText();
//
//        return objectMapper.readValue(text, clazz);
//    }
}
