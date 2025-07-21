package com.kira.Banking_System.service.impl;

import com.kira.Banking_System.dto.BankResponse;
import com.kira.Banking_System.dto.UserRequest;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);
}
