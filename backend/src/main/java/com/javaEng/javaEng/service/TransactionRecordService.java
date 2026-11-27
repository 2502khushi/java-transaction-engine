package com.javaEng.javaEng.service;

import com.javaEng.javaEng.repository.FailedTransactionRepository;
import com.javaEng.javaEng.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.javaEng.javaEng.repository.FailedTransactionRepository;

import java.math.BigDecimal;

@Service
public class TransactionRecordService {

    private final TransactionRepository transactionRepository;
    private final FailedTransactionRepository failedTransactionRepository;

    public TransactionRecordService(
        TransactionRepository transactionRepository,
        FailedTransactionRepository failedTransactionRepository) {

    this.transactionRepository = transactionRepository;
    this.failedTransactionRepository = failedTransactionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createPendingTransaction(
            String transactionId,
            String type,
            BigDecimal amount,
            Long sourceAccount,
            Long destinationAccount) {

        transactionRepository.savePendingTransaction(
                transactionId,
                type,
                amount,
                sourceAccount,
                destinationAccount
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSuccess(String transactionId) {
        transactionRepository.updateTransactionStatus(transactionId, "SUCCESS");
    }

    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    // public void markFailure(String transactionId, String status,
    //                        String errorCategory, String reason) {

    //     transactionRepository.updateTransactionStatus(transactionId, status);

    //     // Failure record is inserted in the same independent transaction.
    // }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
public void markFailure(String transactionId, String status,
                       String errorCategory, String reason) {

    transactionRepository.updateTransactionStatus(transactionId, status);

    failedTransactionRepository.saveFailure(
            transactionId,
            errorCategory,
            reason
    );
}
}