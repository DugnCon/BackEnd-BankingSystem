package com.damdung.banking.entity.account;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_history")
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class TransferHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transferID")
    private TransferEntity transfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccountID", referencedColumnName = "accountID")
    private BankAccountEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toAccountID", referencedColumnName = "accountID")
    private BankAccountEntity receiver;

    @Column(name = "transactionCode")
    private String transactionCode;

    @Column(name = "fee")
    private BigDecimal fee;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "failureReason")
    private String failureReason;

    @Column(name = "createdAt")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "completedAt")
    private LocalDateTime completedAt;

    @Column(name = "message")
    private String message;

    @Column(name = "fromAccountNumber")
    private String fromAccountNumber;

    @Column(name = "toAccountNumber")
    private String toAccountNumber;

    @PostPersist
    public void setChannelAndCategory() {
        String category = "TRANSFER";
        String channel = "INTERNET_BANKING";
    }

}
