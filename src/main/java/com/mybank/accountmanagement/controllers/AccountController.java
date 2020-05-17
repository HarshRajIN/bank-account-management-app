package com.mybank.accountmanagement.controllers;

import com.mybank.accountmanagement.models.Account;
import com.mybank.accountmanagement.models.Transaction;
import com.mybank.accountmanagement.models.User;
import com.mybank.accountmanagement.repositories.AccountRepository;
import com.mybank.accountmanagement.repositories.TransactionRepository;
import com.mybank.accountmanagement.repositories.UserRepository;
import com.mybank.accountmanagement.services.AccountServiceImpl;
import com.mybank.accountmanagement.services.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/account-api")
public class AccountController {
    UserRepository userRepository;
    AccountRepository accountRepository;
    AccountServiceImpl accountService;
    TransactionServiceImpl transactionService;
    TransactionRepository transactionRepository;

    public AccountController(UserRepository userRepository, AccountRepository accountRepository, AccountServiceImpl accountService, TransactionServiceImpl transactionService, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }
    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable("id") Optional<Account> account){
        if(account.isPresent()){
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(Principal principal) {
        logger.info("Inside /accounts");

        Optional<User> user = userRepository.findByUsername(principal.getName());
        if (user.isPresent()) {
            return new ResponseEntity<List<Account>>(accountService.findByUser(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/accounts/create")
    public ResponseEntity<Account> createAccount(Principal principal, @RequestBody Optional<Account> account) throws Exception{
        if(account.isPresent()) {
            Optional<User> user = userRepository.findByUsername(principal.getName());
            if (user.isPresent()) {
                account.get().setUserId(user.get().getId());
                // tries to save before persisting transaction
                accountService.save(account.get());
                Transaction transaction = new Transaction(account.get().getName(), "Initial Deposit", new BigDecimal(0),
                        account.get().getBalance());

                transactionService.save(transaction);


                return new ResponseEntity<Account>(account.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
