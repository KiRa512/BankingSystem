package com.kira.Banking_System.controllers;

import com.kira.Banking_System.dto.*;
import com.kira.Banking_System.service.impl.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }


    @GetMapping("emailEnquiry")
    public BankResponse getAccountByEmail(@RequestBody String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("nameEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest accountNumber) {
        return userService.BalanceEnquiry(accountNumber);
    }

    @GetMapping("creditAccount")
    public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.creditAccount(creditDebitRequest);
    }

    @GetMapping("debitAccount")
    public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
        return userService.debitAccount(creditDebitRequest);
    }

    @PostMapping("transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
        return userService.transfer(transferRequest);
    }
}
