package com.kira.Banking_System.utils;

import com.kira.Banking_System.dto.EmailDetails;
import com.kira.Banking_System.entity.User;
import com.kira.Banking_System.service.impl.EmailService;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmailService {
    EmailService emailService;
    public SimpleEmailService(EmailService emailService) {
        this.emailService = emailService;

    }
    public void sendEmail(User user , String subject, String message) {
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject(subject)
                .body(message)
                .build();
        // Here you would implement the logic to send the email using an email service provider
        emailService.sendEmail(emailDetails);
    }
}
