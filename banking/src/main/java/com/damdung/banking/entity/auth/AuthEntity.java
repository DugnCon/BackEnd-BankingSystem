package com.damdung.banking.entity.auth;

import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.entity.account.TransactionEntity;
import com.damdung.banking.entity.account.TransferEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@DynamicInsert
@Getter
@Setter
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "postalCode", nullable = false)
    private String postalCode;

    @Column(name = "dateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "ssn", nullable = false)
    private String ssn;

    @Column(name = "createdAt", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "loginAt", nullable = false)
    private LocalDateTime loginAt;

    @Column(name = "logoutAt", nullable = false)
    private LocalDateTime logoutAt;

    @OneToOne(mappedBy = "auth", fetch = FetchType.LAZY)
    @JsonBackReference
    private BankAccountEntity accounts;

    @JsonBackReference
    public BankAccountEntity getAccounts() {
        return accounts;
    }
}
