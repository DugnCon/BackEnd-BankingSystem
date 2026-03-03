package com.damdung.banking.utils.QrUtils;
public class EmvParser {

    public EmvNode parse(String emv) {

        if (emv == null || emv.length() < 8) {
            throw new IllegalArgumentException("Invalid EMV string");
        }

        validateCRC(emv);

        EmvNode root = new EmvNode("root", null);

        int index = 0;

        while (index < emv.length() - 4) {

            String id = emv.substring(index, index + 2);
            int length = Integer.parseInt(emv.substring(index + 2, index + 4));
            String value = emv.substring(index + 4, index + 4 + length);

            EmvNode node = new EmvNode(id, value);

            if (isNested(id)) {
                parseChildren(node, value);
            }

            root.addChild(node);

            index += 4 + length;
        }

        return root;
    }

    private void parseChildren(EmvNode parent, String value) {

        int index = 0;

        while (index < value.length()) {

            String id = value.substring(index, index + 2);
            int length = Integer.parseInt(value.substring(index + 2, index + 4));
            String val = value.substring(index + 4, index + 4 + length);

            EmvNode child = new EmvNode(id, val);
            parent.addChild(child);

            index += 4 + length;
        }
    }

    private boolean isNested(String id) {
        int tag = Integer.parseInt(id);
        return id.equals("38")
                || id.equals("62")
                || (tag >= 26 && tag <= 51);
    }

    private void validateCRC(String emv) {

        String withoutCRC = emv.substring(0, emv.length() - 4);
        String crcFromQr = emv.substring(emv.length() - 4);

        String calculated = crc16(withoutCRC);

        if (!crcFromQr.equalsIgnoreCase(calculated)) {
            throw new RuntimeException("Invalid CRC");
        }
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
