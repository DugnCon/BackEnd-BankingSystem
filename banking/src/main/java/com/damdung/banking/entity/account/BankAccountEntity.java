package com.damdung.banking.entity.account;

import com.damdung.banking.entity.auth.AuthEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<TransferEntity> sender_transaction = new HashSet<>();

    @JsonBackReference
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TransferEntity> receiver_transaction = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<TransferHistoryEntity> sender_transfer_histories = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<TransferHistoryEntity> receiver_transfer_histories = new HashSet<>();

    @JsonBackReference
    public Set<TransferEntity> getReceiver_transaction() {
        return receiver_transaction;
    }

    @JsonBackReference
    public Set<TransferEntity> getSender_transaction() {
        return sender_transaction;
    }

    @JsonBackReference
    public void setReceiver_transfer_histories(Set<TransferHistoryEntity> receiver_transfer_histories) {
        this.receiver_transfer_histories = receiver_transfer_histories;
    }

    @JsonBackReference
    public Set<TransferHistoryEntity> getSender_transfer_histories() {
        return sender_transfer_histories;
    }

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
