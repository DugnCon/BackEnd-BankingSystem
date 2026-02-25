package com.damdung.banking.entity.account;

import com.damdung.banking.entity.auth.AuthEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "transfers")
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"sender", "receiver"}) // tránh stack overflow khi log
public class TransferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transferID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromAccountID", referencedColumnName = "accountID")
    private BankAccountEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toAccountID", referencedColumnName = "accountID")
    private BankAccountEntity receiver;

    @OneToMany(mappedBy = "transfer", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    private Set<TransferHistoryEntity> transfer_histories = new HashSet<>();

    @Column(name = "fee", nullable = false)
    private BigDecimal fee;

    @Column(name = "message", nullable = true)
    private String message;

    @Column(name = "createdAt")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
