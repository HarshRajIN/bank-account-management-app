package com.mybank.accountmanagement.controllers;

import com.mybank.accountmanagement.exceptions.BalanceLowException;
import com.mybank.accountmanagement.exceptions.UserNotFoundException;
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
@RequestMapping("/user-api")
public class UserController {

    UserRepository userRepository;
    TransactionRepository transactionRepository;
    TransactionServiceImpl transactionService;
    AccountRepository accountRepository;
    AccountServiceImpl accountService;

    public UserController(UserRepository userRepository, TransactionRepository transactionRepository, TransactionServiceImpl transactionService, AccountRepository accountRepository, AccountServiceImpl accountService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/deposit/{amount}")
    public ResponseEntity<List<Account>> deposit(Principal principal,
                                                 @RequestBody Optional<Account> account,
                                                 @PathVariable("amount") Double amount) throws Exception {
        logger.info("Inside /deposit/{amount}" + " -->" + amount);
        if (account.isPresent()) {
            BigDecimal before = account.get().getBalance();
            account.get().setBalance(new BigDecimal(amount + account.get().getBalance().doubleValue()));
            accountService.save(account.get());

            Optional<User> user = userRepository.findByUsername(principal.getName());

            Transaction transaction = new Transaction(account.get().getName(), "Deposit",before,
                    account.get().getBalance());
            transactionService.save(transaction);

            return new ResponseEntity<List<Account>>(accountService.findByUser(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/withdraw/{amount}")
    public ResponseEntity<List<Account>> withdraw(Principal principal, @RequestBody Optional<Account> account,
                                                  @PathVariable("amount") Double amount) throws Exception{

        logger.info("Inside /withdraw/{amount}" + " -->" + amount);

        if (account.isPresent()) {
            validateWithdrawal(account.get().getBalance().doubleValue(), amount);
            BigDecimal before = account.get().getBalance();
            account.get().setBalance(new BigDecimal(account.get().getBalance().doubleValue()-amount));
            accountService.save(account.get());
            Optional<User> user = userRepository.findByUsername(principal.getName());

            Transaction transaction = new Transaction(account.get().getName(), "Withdrawal",
                    before, account.get().getBalance());
            transactionService.save(transaction);

            return new ResponseEntity<List<Account>>(accountService.findByUser(user.get()), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    private void validateUser(Principal principal){
        String username = principal.getName();
        this.userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(username)
                );
    }

    private void validateWithdrawal(Double userFunds, Double amount){
        if(userFunds < amount){
            throw new BalanceLowException("Insufficient funds");
        }
    }

}
