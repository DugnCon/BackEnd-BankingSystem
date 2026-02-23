package com.damdung.banking.entity.account;

import com.damdung.banking.entity.auth.AuthEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "accounts")
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class BankAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    @Column(name = "accountNumber", unique = true, length = 20)
    private String accountNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false, unique = true)
    private AuthEntity auth;

    @Column(name = "availableBalance", precision = 18, scale = 2)
    private BigDecimal availableBalance;

    @Column(name = "currentBalance", precision = 18, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "officialName", length = 255)
    private String officialName;

    @Column(name = "mask", length = 10)
    private String mask;

    @Column(name = "institutionID", length = 100)
    private String institutionID;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "subtype", nullable = false,length = 50)
    private String subtype;

    @Column(name = "shareableID", unique = true, length = 100)
    private String shareableID;

    @Column(name = "currency", length = 10)
    private String currency = "VND";

    @Column(name = "status", length = 30)
    private String status = "ACTIVE";

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.availableBalance == null) {
            this.availableBalance = BigDecimal.ZERO;
        }
        if (this.currentBalance == null) {
            this.currentBalance = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
