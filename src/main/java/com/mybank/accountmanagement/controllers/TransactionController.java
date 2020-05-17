package com.mybank.accountmanagement.controllers;

import com.mybank.accountmanagement.exceptions.UserNotFoundException;
import com.mybank.accountmanagement.models.Transaction;
import com.mybank.accountmanagement.repositories.UserRepository;
import com.mybank.accountmanagement.services.TransactionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transaction-api")
public class TransactionController {

    TransactionServiceImpl transactionService;
    UserRepository userRepository;

    public TransactionController(TransactionServiceImpl transactionService, UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }
    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> history(Principal principal){
        logger.info("Getting all txns...");
        validateUser(principal);

        return new ResponseEntity<List<Transaction>>(transactionService.findAllByCreatedBy(principal.getName()),
                HttpStatus.OK);
    }
    private void validateUser(Principal principal){
        String username = principal.getName();
        this.userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException(username)
                );
    }
}

