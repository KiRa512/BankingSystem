package com.kira.Banking_System.service.impl;

import com.kira.Banking_System.dto.*;
import com.kira.Banking_System.entity.User;
import com.kira.Banking_System.repository.UserRepo;
import com.kira.Banking_System.utils.AccountUtils;
import com.kira.Banking_System.utils.SimpleEmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final EmailService emailService;
    private final SimpleEmailService simpleEmailService;

    public UserServiceImpl(UserRepo userRepo, EmailService emailService, SimpleEmailService simpleEmailService) {
        this.userRepo = userRepo;
        this.emailService =  emailService;
        this.simpleEmailService = simpleEmailService;
    }


    @Override
    public BankResponse createUser(UserRequest userRequest) {
        // Validate the user exists
        if(userRepo.existsByEmail(userRequest.getEmail())) {
            BankResponse response = BankResponse.builder()
                    .ResponseCode(AccountUtils.ACCOUNT_EXISTS_NO)
                    .ResponseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
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

        // Save the new user to the repository
        User savedUser = userRepo.save(newUser);

        // Send a welcome email to the user
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome to Our Banking System")
                .body("Dear " + savedUser.getFirstname() + ",\n\n" +
                      "Thank you for creating an account with us. Your account number is " + savedUser.getAccountNumber() + ".\n" +
                      "We are excited to have you on board!\n\n" +
                      "Best regards,\n" +
                      "The Banking System Team")
                .build();
        emailService.sendEmail(emailDetails);

        // Create a response indicating success
        BankResponse response = BankResponse.builder()
                .ResponseCode(AccountUtils.ACCOUNT_CREATED_SUCCESSFULLY)
                .ResponseMessage(AccountUtils.ACCOUNT_CREATED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedUser.getFirstname() + " " + savedUser.getLastname())
                        .balance(savedUser.getBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .build())
                .build();
                return response;
    }

    @Override
    public BankResponse getUserByEmail(String email){
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return BankResponse.builder()
                    .ResponseCode(AccountUtils.USER_NOT_FOUND_CODE)
                    .ResponseMessage(AccountUtils.USER_NOT_FOUND_MESSAGE)
                    .build();
        }
        return BankResponse.builder()
                .ResponseCode("00")
                .ResponseMessage("User found successfully")
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstname() + " " + user.getLastname())
                        .balance(user.getBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse BalanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean AccountExist = userRepo.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!AccountExist) {
            return BankResponse.builder()
                    .ResponseCode(AccountUtils.USER_NOT_FOUND_CODE)
                    .ResponseMessage(AccountUtils.USER_NOT_FOUND_MESSAGE)
                    .build();
        }
        User user = userRepo.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .ResponseCode("00")
                .ResponseMessage("Balance enquiry successful")
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstname() + " " + user.getLastname())
                        .balance(user.getBalance())
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }

    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        boolean accountExists = userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!accountExists) {
            return BankResponse.builder()
                    .ResponseCode(AccountUtils.USER_NOT_FOUND_CODE)
                    .ResponseMessage(AccountUtils.USER_NOT_FOUND_MESSAGE)
                    .build();
        }
        User user = userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigDecimal updatedBalance = user.getBalance().add(creditDebitRequest.getAmount());
        user.setBalance(updatedBalance);
        userRepo.save(user);
        return BankResponse.builder()
                .ResponseCode(AccountUtils.CREDIT_SUCCESS_CODE)
                .ResponseMessage(AccountUtils.CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstname() + " " + user.getLastname())
                        .balance(updatedBalance)
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        boolean accountExists = userRepo.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!accountExists) {
            return BankResponse.builder()
                    .ResponseCode(AccountUtils.USER_NOT_FOUND_CODE)
                    .ResponseMessage(AccountUtils.USER_NOT_FOUND_MESSAGE)
                    .build();
        }
        User user = userRepo.findByAccountNumber(creditDebitRequest.getAccountNumber());
        BigDecimal currentBalance = user.getBalance();
        if (currentBalance.compareTo(creditDebitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .ResponseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .ResponseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .build();
        }
        BigDecimal updatedBalance = currentBalance.subtract(creditDebitRequest.getAmount());
        user.setBalance(updatedBalance);
        userRepo.save(user);
        return BankResponse.builder()
                .ResponseCode("00")
                .ResponseMessage("Debit successful")
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstname() + " " + user.getLastname())
                        .balance(updatedBalance)
                        .accountNumber(user.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public BankResponse transfer(TransferRequest transferRequest) {
        User sourceUser = userRepo.findByAccountNumber(transferRequest.getSourceAccountNumber());
        User destinationUser = userRepo.findByAccountNumber(transferRequest.getDestinationAccountNumber());

        if (sourceUser == null || destinationUser == null) {
            return BankResponse.builder()
                    .ResponseCode(AccountUtils.USER_NOT_FOUND_CODE)
                    .ResponseMessage(AccountUtils.USER_NOT_FOUND_MESSAGE)
                    .build();
        }

        BigDecimal amount = new BigDecimal(transferRequest.getAmount());

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return BankResponse.builder()
                    .ResponseCode("99")
                    .ResponseMessage("Invalid transfer amount")
                    .build();
        }

        if (sourceUser.getBalance().compareTo(amount) < 0) {
            return BankResponse.builder()
                    .ResponseCode(AccountUtils.INSUFFICIENT_FUNDS_CODE)
                    .ResponseMessage(AccountUtils.INSUFFICIENT_FUNDS_MESSAGE)
                    .build();
        }

        // Perform transfer
        sourceUser.setBalance(sourceUser.getBalance().subtract(amount));
        destinationUser.setBalance(destinationUser.getBalance().add(amount));
        userRepo.save(sourceUser);
        userRepo.save(destinationUser);

        // Send emails
        simpleEmailService.sendEmail(sourceUser,"Transfer Success" ,"Transfer Notification" + ".\n" +
                "Dear " + sourceUser.getFirstname() + ",\n\n" +
                "You have successfully transferred " + amount + " to " + destinationUser.getFirstname() + ".\n" +
                "Your new balance is " + sourceUser.getBalance() + ".\n\n" +
                "Best regards,\n" +
                "The Banking System Team");

        simpleEmailService.sendEmail(destinationUser,"Transfer Recieved" ,"Transfer Received Notification" + ".\n" +
                "Dear " + destinationUser.getFirstname() + ",\n\n" +
                "You have successfully Recieved " + amount + " to " + sourceUser.getFirstname() + ".\n" +
                "Your new balance is " + destinationUser.getBalance() + ".\n\n" +
                "Best regards,\n" +
                "The Banking System Team");



        return BankResponse.builder()
                .ResponseCode("00")
                .ResponseMessage("Transfer successful")
                .accountInfo(AccountInfo.builder()
                        .accountName(sourceUser.getFirstname() + " " + sourceUser.getLastname())
                        .balance(sourceUser.getBalance())
                        .accountNumber(sourceUser.getAccountNumber())
                        .build())
                .build();
        }

    }
