package com.damdung.banking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class IdempotencyEntity {

    @Id
    private String idempotencyID;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Lob
    private String responseBody;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Status {
        PROCESSING,
        SUCCESS,
        FAILED
    }
}
