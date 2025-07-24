package com.kira.Banking_System.utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_NO = "01";
    public static final String ACCOUNT_EXISTS_MESSAGE = "User with this email already exists";

    public static final String ACCOUNT_CREATED_SUCCESSFULLY = "02";
    public static final String ACCOUNT_CREATED_MESSAGE = "User Created Successfully";

    public static final String USER_NOT_FOUND_CODE = "404";
    public static final String USER_NOT_FOUND_MESSAGE = "No user found with this email";

    public static final String NO_USERS_FOUND_CODE = "404";
    public static final String NO_USERS_FOUND_MESSAGE = "No users found in the system";

    public static final String CREDIT_SUCCESS_CODE = "002";
    public static final String CREDIT_SUCCESS_MESSAGE = "Credit successful";
    public static final String INSUFFICIENT_FUNDS_CODE = "003";
    public static final String INSUFFICIENT_FUNDS_MESSAGE = "Insufficient funds for this operation";


    public static String generateAccountNumber() {
        return Year.now() + String.valueOf(System.currentTimeMillis()).substring(5);
    }
}
