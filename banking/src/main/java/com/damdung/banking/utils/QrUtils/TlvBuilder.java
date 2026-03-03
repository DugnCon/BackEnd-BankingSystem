package com.damdung.banking.utils.QrUtils;

import org.springframework.stereotype.Component;

@Component
public class TlvBuilder {
    private final StringBuilder sb = new StringBuilder();

    public TlvBuilder add(String id, String value) {
        sb.append(id)
                .append(String.format("%02d", value.length()))
                .append(value);
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
