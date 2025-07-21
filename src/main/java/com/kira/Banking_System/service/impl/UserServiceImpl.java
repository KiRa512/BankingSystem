package com.kira.Banking_System.service.impl;

import com.kira.Banking_System.dto.BankResponse;
import com.kira.Banking_System.dto.UserRequest;
import com.kira.Banking_System.entity.User;
import com.kira.Banking_System.repository.UserRepo;
import com.kira.Banking_System.utils.AccountUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public BankResponse createUser(UserRequest userRequest) {
        // Validate the user exists
        if(userRepo.existsByEmail(userRequest.getEmail())) {
            BankResponse response = new BankResponse();
            response.setResponseCode(AccountUtils.ACCOUNT_EXISTS_NO);
            response.setResponseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE);
            return response;
        }


        User newUser = User.builder()
                .firstname(userRequest.getFirstname())
                .lastname(userRequest.getLastname())
                .email(userRequest.getEmail())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .address(userRequest.getAddress())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtils.generateAccountNumber()) // Assume this method generates a unique account number
                .status("ACTIVE") // Default status set to ACTIVE
                .balance(BigDecimal.ZERO) // Default balance set to zero
                .build();

        BankResponse response = new BankResponse();
        response.setResponseCode("00");
        response.setResponseMessage("User created successfully");
        return response;
    }
}
