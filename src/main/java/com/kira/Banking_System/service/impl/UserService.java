package com.kira.Banking_System.service.impl;

import com.kira.Banking_System.dto.*;

import java.util.List;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);
    BankResponse getUserByEmail(String email);
    BankResponse BalanceEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
    BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
    BankResponse transfer(TransferRequest transferRequest);
}
