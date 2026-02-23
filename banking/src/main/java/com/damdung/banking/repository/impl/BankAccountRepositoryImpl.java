package com.damdung.banking.repository.impl;

import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.model.dto.GetBankAccountDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.repository.custom.BankAccountRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class BankAccountRepositoryImpl implements BankAccountRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    
    /**
     * Kiểm tra có tài khoản chưa, kiểu List thì dùng Tuple
     * */
    @Override
    public Map<String, Object> isAccountExists(MyUserDetail myUserDetail) {
        Long userID = myUserDetail.getUserID();

        String q = "select count(*) from accounts where userID = :userID";
        Query qr = entityManager.createNativeQuery(q);

        qr.setParameter("userID", userID);

        Number checkAccounts = (Number) qr.getSingleResult();
        Long hasAccounts = checkAccounts.longValue();

        return Map.of("hasAccounts", hasAccounts);
    }

    /**
     * Lấy thông tin accounts từ người dùng
     * */
    @Override
    public GetBankAccountDTO getAccounts(MyUserDetail myUserDetail) {
        String jpql = "SELECT new com.damdung.banking.model.dto.GetBankAccountDTO( " +
                "b.accountID, " +
                "b.auth.userID, " +
                "b.accountNumber, " +
                "b.officialName, " +
                "b.shareableID, " +
                "b.name, " +
                "b.type, " +
                "b.mask, " +
                "b.availableBalance, " +
                "b.currentBalance, " +
                "b.currency " +
                ") " +
                "FROM BankAccountEntity b " +
                "WHERE b.auth.userID = :userID";
        return entityManager.createQuery(jpql, GetBankAccountDTO.class)
                .setParameter("userID", myUserDetail.getUserID())
                .getSingleResult();
    }
}
