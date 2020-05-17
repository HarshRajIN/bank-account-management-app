package com.mybank.accountmanagement.services;

import com.mybank.accountmanagement.models.Account;
import com.mybank.accountmanagement.models.User;

import java.util.List;

public interface IAccountService {

    public Account save(Account account) throws Exception;
    public List<Account> findByUser(User user);
}