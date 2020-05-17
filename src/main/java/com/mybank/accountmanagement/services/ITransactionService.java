package com.mybank.accountmanagement.services;

import com.mybank.accountmanagement.models.Transaction;

import java.util.List;

public interface ITransactionService{
    Transaction save(Transaction transaction) throws Exception;
    List<Transaction> findAllByCreatedBy(String username);
}
