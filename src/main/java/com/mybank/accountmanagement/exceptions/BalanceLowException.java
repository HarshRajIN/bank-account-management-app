package com.mybank.accountmanagement.exceptions;

public class BalanceLowException extends RuntimeException {
    public BalanceLowException(String message) {
        super(message);
    }
}
