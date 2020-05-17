package com.mybank.accountmanagement;

import com.mybank.accountmanagement.exceptions.BalanceLowException;
import com.mybank.accountmanagement.exceptions.UserNotFoundException;
import com.mybank.accountmanagement.models.Account;
import com.mybank.accountmanagement.models.Transaction;
import com.mybank.accountmanagement.models.User;
import com.mybank.accountmanagement.repositories.AccountRepository;
import com.mybank.accountmanagement.repositories.UserRepository;
import com.mybank.accountmanagement.services.AccountServiceImpl;
import com.mybank.accountmanagement.services.TransactionServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class ViewController {

    UserRepository userRepository;
    TransactionServiceImpl transactionService;
    AccountRepository accountRepository;
    AccountServiceImpl accountService;

    public ViewController(UserRepository userRepository, TransactionServiceImpl transactionService, AccountRepository accountRepository, AccountServiceImpl accountService) {
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsername(Principal principal){
        Optional<User> user = userRepository.findByUsername(principal.getName());

        if(user.isPresent()){

            return new ResponseEntity<String>(user.get().getUsername(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<List<Account>> dashboard(Principal principal){
        validateUser(principal);
        Optional<User> user = userRepository.findByUsername(principal.getName());


        if(user.isPresent()){
            List<Account> accounts = accountService.findByUser(user.get());
            return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> history(Principal principal){
        validateUser(principal);

        return new ResponseEntity<List<Transaction>>(transactionService.findAllByCreatedBy(principal.getName()),
                HttpStatus.OK);
    }

    @GetMapping("/logout")
    public String logout(){
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }

    @RequestMapping("/")
    public String sendToLogin(){
        return "login";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "login";
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
            throw new BalanceLowException("insufficient funds");
        }
    }

}
