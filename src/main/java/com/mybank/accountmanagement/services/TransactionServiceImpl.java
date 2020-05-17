package com.mybank.accountmanagement.services;

import com.mybank.accountmanagement.models.Transaction;
import com.mybank.accountmanagement.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements ITransactionService{
    TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllByCreatedBy(String username) {
        return transactionRepository.findByCreatedBy(username);
    }
}
