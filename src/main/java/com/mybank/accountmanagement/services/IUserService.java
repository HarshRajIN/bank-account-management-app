package com.mybank.accountmanagement.services;

import com.mybank.accountmanagement.models.User;

import java.math.BigDecimal;

public interface IUserService {
    User depositCurrent(User user, BigDecimal amount);
    User depositSavings(User user, BigDecimal amount);
    User withdrawSavings(User user, BigDecimal amount);
    User withdrawCurrent(User user, BigDecimal amount);
    BigDecimal checkSavings(User user);
    BigDecimal checkCurrent(User user);
    void transferToSavings(User user, BigDecimal amount);
    void transferToCurrent(User user, BigDecimal amount);
}