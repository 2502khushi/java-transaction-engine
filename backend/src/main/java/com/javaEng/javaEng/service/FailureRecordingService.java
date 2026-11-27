package com.javaEng.javaEng.service;

import com.javaEng.javaEng.repository.FailedTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FailureRecordingService {

    private final FailedTransactionRepository failedTransactionRepository;

    public FailureRecordingService(FailedTransactionRepository failedTransactionRepository) {
        this.failedTransactionRepository = failedTransactionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(String transactionId, String errorCategory, String reason) {
        failedTransactionRepository.saveFailure(
                transactionId,
                errorCategory,
                reason
        );
    }
}