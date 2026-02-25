package com.damdung.banking.service.impl;

import com.damdung.banking.annotation.LoggingAnnotation;
import com.damdung.banking.config.kafka.KafkaProducerConfig;
import com.damdung.banking.entity.account.BankAccountEntity;
import com.damdung.banking.entity.account.TransactionEntity;
import com.damdung.banking.entity.account.TransferEntity;
import com.damdung.banking.entity.account.TransferHistoryEntity;
import com.damdung.banking.entity.auth.AuthEntity;
import com.damdung.banking.exception.BusinessException;
import com.damdung.banking.model.dto.CreateBankAccountDTO;
import com.damdung.banking.model.dto.CreateTransactionsDTO;
import com.damdung.banking.model.dto.GetBankAccountDTO;
import com.damdung.banking.model.dto.MyUserDetail;
import com.damdung.banking.model.dto.event.TransferEventDTO;
import com.damdung.banking.repository.*;
import com.damdung.banking.repository.custom.BankAccountRepositoryCustom;
import com.damdung.banking.service.IBankService;
import com.damdung.banking.service.IdempotencyService;
import com.damdung.banking.service.kafka.TransactionProducer;
import com.damdung.banking.service.rabbitmq.email.EmailTransactionProducer;
import com.damdung.banking.utils.MapUtils;
import com.damdung.banking.utils.SQLUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@LoggingAnnotation
public class BankServiceImpl implements IBankService {
    @Autowired
    private IBankAccountRepository bankAccountRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SQLUtils sqlUtils;
    @Autowired
    private BankAccountRepositoryCustom bankAccountRepositoryCustom;
    @Autowired
    private IAuthRepository authRepository;
    @Autowired
    private IdempotencyService idempotencyService;
    @Autowired
    private ITransactionRepository transactionRepository;
    @Autowired
    private ITransferRepository transferRepository;
    @Autowired
    private TransactionProducer transactionProducer;
    @Autowired
    private ITransferHistoryRepository transferHistoryRepository;
    @Autowired
    private EmailTransactionProducer emailTransactionProducer;
    /**
     * Tạo tài khoản account
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Object> createBankAccount(MyUserDetail myUserDetail, CreateBankAccountDTO bankAccountDTO) {
        AuthEntity auth = sqlUtils.findAuth(myUserDetail.getUserID());
        BankAccountEntity bankAccount = modelMapper.map(bankAccountDTO, BankAccountEntity.class);

        bankAccount.setAuth(auth);

        bankAccountRepository.save(bankAccount);

        String accountNumber = "9704" +
                String.format("%010d", bankAccount.getAccountID());

        bankAccount.setAccountNumber(accountNumber);
        bankAccount.setShareableID(accountNumber);
        bankAccount.setMask(accountNumber.substring(10));

        return ResponseEntity.ok(Map.of("message", "Account created successfully", "success", true));
    }

    /**
     * Kiểm tra tồn tại tài khoản
     * */
    @Override
    public ResponseEntity<Object> isAccountExists(MyUserDetail myUserDetail) {
        Map<String, Object> result = bankAccountRepositoryCustom.isAccountExists(myUserDetail);

        Long hasAccounts = MapUtils.getObject(result, "hasAccounts", Long.class);

        if(hasAccounts > 0) {
            return ResponseEntity.ok(Map.of("hasAccounts", true, "totalBanks", hasAccounts));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("hasAccounts", false, "totalBanks", 0));
    }

    /**
     * Lấy dữ liệu accounts của người dùng
     * */
    @Override
    public ResponseEntity<Object> getAccounts(MyUserDetail myUserDetail) {
        GetBankAccountDTO bankAccountDTO = bankAccountRepositoryCustom.getAccounts(myUserDetail);

        if(bankAccountDTO == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("data", bankAccountDTO, "totalBanks", 0, "totalCurrentBalance", 0));

        BigDecimal currentBalance = bankAccountDTO.currentBalance();

        Map<String, Object> result = bankAccountRepositoryCustom.isAccountExists(myUserDetail);
        Long totalBanks = MapUtils.getObject(result, "hasAccounts", Long.class);

        assert totalBanks != null;
        return ResponseEntity.ok(Map.of("data", bankAccountDTO, "totalBanks", totalBanks, "totalCurrentBalance", currentBalance));
    }

    /**
     * Thực hiện giao dịch
     * */
    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ResponseEntity<Object> createTransactions(Map<String, Object> object, CreateTransactionsDTO createTransactionsDTO, String key) {

        BankAccountEntity sender  = (BankAccountEntity) object.get("sender");
        BankAccountEntity receiver  = (BankAccountEntity) object.get("receiver");

        try {

            BigDecimal amount = createTransactionsDTO.getAmount();
            TransferEntity transfer = new TransferEntity();

            BigDecimal senderAvailableResult = sender.getAvailableBalance().subtract(amount);
            BigDecimal senderCurrentResult   = sender.getCurrentBalance().subtract(amount);

            BigDecimal receiverAvailableResult = receiver.getAvailableBalance().add(amount);
            BigDecimal receiverCurrentResult   = receiver.getCurrentBalance().add(amount);

            sender.setAvailableBalance(senderAvailableResult);
            sender.setCurrentBalance(senderCurrentResult);

            receiver.setAvailableBalance(receiverAvailableResult);
            receiver.setCurrentBalance(receiverCurrentResult);

            transfer.setSender(sender);
            transfer.setReceiver(receiver);
            transfer.setFee(amount);
            transfer.setMessage(createTransactionsDTO.getNote());

            bankAccountRepository.save(sender);
            bankAccountRepository.save(receiver);
            transferRepository.save(transfer);

            Map<String,Object> message = Map.of("transfer", transfer
                    , "emailSender", sender.getAuth().getEmail()
                    , "emailReciver", receiver.getAuth().getEmail());

            idempotencyService.markSuccess(key, "Successed transaction!");

            /**
             * Lưu lịch sử giao dịch
             * */
            saveTransactionHistory(transfer);

            /**
             * Làm DTO gửi email thông qua RabbitMQ
             * */
            TransferEventDTO transferEventDTO = new TransferEventDTO();

            transferEventDTO.setFee(transfer.getFee());
            transferEventDTO.setReceiverEmail(receiver.getAuth().getEmail());
            transferEventDTO.setReceiverAccountNumber(receiver.getAccountNumber());
            transferEventDTO.setSenderEmail(sender.getAuth().getEmail());
            transferEventDTO.setSenderAccountNumber(sender.getAccountNumber());

            emailTransactionProducer.sendMessage(transferEventDTO);

            return ResponseEntity.ok(Map.of("success", true, "message", "Successed transaction!"));
        } catch (Exception e) {
            log.error("Lỗi xử lý giao dịch với idempotency key: {}", key, e);
            idempotencyService.markFailed(key, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false
                    , "message", "The system is experiencing issues. Please try again!"));
        }
    }

    @Transactional
    public void saveTransactionHistory(TransferEntity transfer) {
        try {
            TransferHistoryEntity transferHistory = new TransferHistoryEntity();

            transferHistory.setTransfer(transfer);
            transferHistory.setSender(transfer.getSender());
            transferHistory.setReceiver(transfer.getReceiver());
            transferHistory.setStatus("COMPLETED");
            transferHistory.setFromAccountNumber(transfer.getSender().getAccountNumber());
            transferHistory.setToAccountNumber(transfer.getReceiver().getAccountNumber());

            transferHistoryRepository.save(transferHistory);
            log.info("Đã lưu history thành công với ID: {}", transferHistory.getHistoryID());

        } catch (Exception e) {
            log.error("Lỗi chi tiết khi lưu history: ", e);
            throw new BusinessException("Failed to save transaction history!");
        }
    }
}
