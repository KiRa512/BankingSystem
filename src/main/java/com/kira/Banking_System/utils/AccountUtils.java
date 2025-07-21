package com.kira.Banking_System.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_NO = "01";
    public static final String ACCOUNT_EXISTS_MESSAGE = "User with this email already exists";

    public static String generateAccountNumber() {
        return Year.now() + String.valueOf(System.currentTimeMillis()).substring(5);
    }
}
