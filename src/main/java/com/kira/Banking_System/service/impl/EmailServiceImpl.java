package com.kira.Banking_System.service.impl;

import com.kira.Banking_System.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender javaMailSender;
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Value("${spring.mail.username}")
    private String senderEmail;
    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(senderEmail);
            simpleMailMessage.setTo(emailDetails.getRecipient());
            simpleMailMessage.setSubject(emailDetails.getSubject());
            simpleMailMessage.setText(emailDetails.getBody());

            javaMailSender.send(simpleMailMessage);
            System.out.println("Email sent successfully to " + emailDetails.getRecipient());
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
