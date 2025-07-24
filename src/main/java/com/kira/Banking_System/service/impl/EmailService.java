package com.kira.Banking_System.service.impl;

import com.kira.Banking_System.dto.EmailDetails;

public interface EmailService {
    void sendEmail(EmailDetails emailDTO);
}
