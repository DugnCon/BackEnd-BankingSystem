package com.damdung.banking.utils.QrUtils;

import org.springframework.stereotype.Component;

@Component
public class EmvBuilder {

    private final TlvBuilder tlv = new TlvBuilder();

    public EmvBuilder add(String id, String value) {
        tlv.add(id, value);
        return this;
    }

    public String build() {
        String payload = tlv.build();
        String withCrcTag = payload + "6304";
        String crc = crc16(withCrcTag);
        return withCrcTag + crc;
    }

    private String crc16(String data) {
        int crc = 0xFFFF;

        for (int i = 0; i < data.length(); i++) {
            crc ^= data.charAt(i) << 8;
            for (int j = 0; j < 8; j++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
            }
        }

        crc &= 0xFFFF;
        return String.format("%04X", crc);
    }
}
