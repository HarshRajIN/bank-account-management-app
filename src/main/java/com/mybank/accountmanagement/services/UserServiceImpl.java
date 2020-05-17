package com.mybank.accountmanagement.services;

import com.mybank.accountmanagement.models.User;
import com.mybank.accountmanagement.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements IUserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User depositSavings(User user, BigDecimal amount) {
        user.setSavingsBalance(user.getSavingsBalance().add(amount));
        return userRepository.save(user);
    }
    @Override
    public User depositCurrent(User user, BigDecimal amount) {
        user.setCurrentBalance(user.getCurrentBalance().add(amount));
        return userRepository.save(user);
    }

    @Override
    public User withdrawSavings(User user, BigDecimal amount) {
        user.setSavingsBalance(user.getSavingsBalance().subtract(amount));
        return userRepository.save(user);
    }

    @Override
    public User withdrawCurrent(User user,  BigDecimal amount) {
        user.setCurrentBalance(user.getCurrentBalance().subtract(amount));
        return userRepository.save(user);
    }

    @Override
    public BigDecimal checkSavings(User user) {
        return user.getSavingsBalance();
    }

    @Override
    public BigDecimal checkCurrent(User user) {
        return user.getCurrentBalance();
    }

    @Override
    public void transferToSavings(User user, BigDecimal amount) {

    }

    @Override
    public void transferToCurrent(User user, BigDecimal amount) {

    }
}
