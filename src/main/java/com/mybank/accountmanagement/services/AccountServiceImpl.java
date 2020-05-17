package com.mybank.accountmanagement.services;

import com.mybank.accountmanagement.models.Account;
import com.mybank.accountmanagement.models.User;
import com.mybank.accountmanagement.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class AccountServiceImpl implements IAccountService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @Override
    public Account save(Account account) throws Exception {
        return accountRepository.save(account);
    }

    @Override
    public List<Account> findByUser(User user) {
        return accountRepository.findByUserId(user.getId());
    }
}